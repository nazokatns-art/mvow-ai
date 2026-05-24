package uz.mentorai.focus.ui.negotiation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import uz.mentorai.focus.agent.PromptTemplates
import uz.mentorai.focus.agent.ToneLevel
import uz.mentorai.focus.agent.client.AnthropicClient
import uz.mentorai.focus.agent.client.Message
import uz.mentorai.focus.agent.client.MessagesRequest
import uz.mentorai.focus.agent.client.Models
import uz.mentorai.focus.agent.client.StreamEvent
import uz.mentorai.focus.agent.client.ToolChoice
import uz.mentorai.focus.agent.fsm.Decision
import uz.mentorai.focus.agent.fsm.LlmRecommendation
import uz.mentorai.focus.agent.fsm.NegotiationContext
import uz.mentorai.focus.agent.fsm.NegotiationFsm
import uz.mentorai.focus.agent.fsm.PreCheckResult
import uz.mentorai.focus.agent.voice.SttEngine
import uz.mentorai.focus.agent.voice.SttState
import uz.mentorai.focus.agent.voice.TtsEngine
import uz.mentorai.focus.data.language.LanguageRepository
import uz.mentorai.focus.data.session.SessionRepository
import uz.mentorai.focus.data.stats.StatsRepository
import uz.mentorai.focus.guard.policy.PolicyEngine
import uz.mentorai.focus.i18n.MentorPhrases
import javax.inject.Inject

@HiltViewModel
class NegotiationViewModel @Inject constructor(
    private val anthropicClient: AnthropicClient,
    private val sessionRepository: SessionRepository,
    private val statsRepository: StatsRepository,
    private val policyEngine: PolicyEngine,
    private val languageRepository: LanguageRepository,
    private val negotiationFsm: NegotiationFsm,
    val ttsEngine: TtsEngine,
    val sttEngine: SttEngine,
    private val moshi: Moshi
) : ViewModel() {

    private val toolInputAdapter: JsonAdapter<Map<String, Any>> =
        moshi.adapter(Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java))

    private val _state = MutableStateFlow<NegotiationUiState>(NegotiationUiState.Initializing)
    val state: StateFlow<NegotiationUiState> = _state.asStateFlow()

    private var streamJob: Job? = null

    fun start() {
        ttsEngine.init()
        viewModelScope.launch {
            ttsEngine.awaitReady()
            val active = sessionRepository.activeSession.first()
            if (active == null) {
                _state.value = NegotiationUiState.Closed("Faol sessiya yo'q")
                return@launch
            }

            val now = System.currentTimeMillis()
            val elapsed = ((now - active.startedAtMillis) / 60_000L).toInt()
            val remaining = ((active.plannedEndAtMillis - now) / 60_000L).toInt().coerceAtLeast(0)

            val context = NegotiationContext(
                session = active,
                minutesElapsed = elapsed,
                minutesRemaining = remaining,
                requestsToday = active.overrideCount,
                streakDays = 0, // TODO Sprint 4 — streak repository
                appCategory = "social_media",
                interceptsToday = active.interceptCount
            )

            val lang = languageRepository.current.value
            when (val pre = negotiationFsm.preCheck(context)) {
                is PreCheckResult.HardDeny -> {
                    speakAndClose(MentorPhrases.quotaExceeded(lang))
                }
                is PreCheckResult.SoftDeny -> {
                    speakAndClose(
                        MentorPhrases.tooEarly(
                            lang,
                            context.minutesElapsed,
                            NegotiationFsm.MIN_MINUTES_BEFORE_NEGOTIATION
                        )
                    )
                }
                is PreCheckResult.RequireVocalCommitment -> {
                    val phrase = MentorPhrases.commitmentPhrase(lang)
                    _state.value = NegotiationUiState.AwaitingCommitment(
                        phrase = phrase,
                        sessionContext = context,
                        grantOnSuccess = pre.onSuccessGrantMinutes
                    )
                    ttsEngine.speak(phrase)
                }
                PreCheckResult.AllowLlmConsultation -> {
                    _state.value = NegotiationUiState.AwaitingReason(context)
                    ttsEngine.speak(MentorPhrases.negotiationOpening(lang))
                }
            }
        }
    }

    fun startListening() {
        viewModelScope.launch {
            sttEngine.start(languageRepository.current.value.sttLocaleTag)
            sttEngine.state.collectLatest { sttState ->
                when (sttState) {
                    is SttState.Final -> handleSpokenReason(sttState.text)
                    is SttState.Error -> {
                        // Foydalanuvchiga sodda xabar — hozircha state'ni o'zgartirmaymiz
                    }
                    else -> Unit
                }
            }
        }
    }

    fun cancelListening() {
        sttEngine.stop()
    }

    private fun handleSpokenReason(reason: String) {
        sttEngine.stop()
        val current = _state.value
        when (current) {
            is NegotiationUiState.AwaitingReason -> consultLlm(current.context, reason)
            is NegotiationUiState.AwaitingCommitment -> verifyCommitment(current, reason)
            else -> Unit
        }
    }

    private fun verifyCommitment(
        state: NegotiationUiState.AwaitingCommitment,
        spoken: String
    ) {
        val lang = languageRepository.current.value
        val matches = stringSimilarity(state.phrase, spoken) > 0.6
        if (matches) {
            grantOverride(state.sessionContext, state.grantOnSuccess, streakPenalty = true)
            speakAndClose(MentorPhrases.grantSmall(lang, state.grantOnSuccess))
        } else {
            speakAndClose(MentorPhrases.hardDeny(lang))
        }
    }

    private fun consultLlm(context: NegotiationContext, spokenReason: String) {
        val lang = languageRepository.current.value
        val sanitized = sanitize(spokenReason)
        if (sanitized.startsWith("[FILTERED")) {
            speakAndClose(MentorPhrases.jailbreakRefused(lang))
            return
        }

        _state.value = NegotiationUiState.ConsultingLlm(context, sanitized)

        val tone = ToneLevel.fromStreakDays(context.streakDays)
        val systemPrompt = PromptTemplates.mentorSystemPrompt(
            language = lang,
            statedGoal = context.session.goal,
            minutesElapsed = context.minutesElapsed,
            minutesRemaining = context.minutesRemaining,
            interceptsToday = context.interceptsToday,
            streakDays = context.streakDays,
            requestsToday = context.requestsToday,
            toneLevel = tone
        )

        val request = MessagesRequest(
            model = Models.HAIKU_4_5,
            maxTokens = 512,
            system = systemPrompt,
            messages = listOf(
                Message(role = "user", content = "Foydalanuvchining sababi: \"$sanitized\"")
            ),
            tools = listOf(PromptTemplates.verdictRecommendationTool),
            toolChoice = ToolChoice(type = "tool", name = "recommend_verdict"),
            temperature = 0.5
        )

        val partialJson = StringBuilder()
        streamJob?.cancel()
        streamJob = viewModelScope.launch {
            anthropicClient.streamMessage(request).collectLatest { event ->
                when (event) {
                    is StreamEvent.ToolUseInputJson -> partialJson.append(event.partialJson)
                    is StreamEvent.MessageStop -> finalizeRecommendation(context, partialJson.toString())
                    is StreamEvent.Error -> {
                        speakAndClose(MentorPhrases.parsingFailed(languageRepository.current.value))
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun finalizeRecommendation(context: NegotiationContext, fullJson: String) {
        val lang = languageRepository.current.value
        val parsed = try {
            toolInputAdapter.fromJson(fullJson) ?: emptyMap()
        } catch (e: Exception) {
            speakAndClose(MentorPhrases.parsingFailed(lang))
            return
        }

        val recommendation = LlmRecommendation(
            isGenuineEmergency = parsed["is_genuine_emergency"] as? Boolean ?: false,
            isRationalization = parsed["is_rationalization"] as? Boolean ?: true,
            category = parsed["category"] as? String ?: "vague",
            confidence = (parsed["confidence"] as? Double) ?: 0.5,
            spokenResponse = parsed["spoken_response"] as? String
                ?: MentorPhrases.hardDeny(lang)
        )

        // Mentor javobini avval gapir
        ttsEngine.speak(recommendation.spokenResponse)

        // FSM yakuniy qaror
        when (val decision = negotiationFsm.finalDecide(context, recommendation)) {
            is Decision.Grant -> {
                grantOverride(
                    context = context,
                    minutes = decision.minutes,
                    streakPenalty = decision.streakPenalty
                )
                _state.value = NegotiationUiState.GrantedClosing(
                    minutes = decision.minutes,
                    spokenResponse = recommendation.spokenResponse
                )
            }
            Decision.HardDeny -> {
                _state.value = NegotiationUiState.DeniedClosing(recommendation.spokenResponse)
            }
            is Decision.SoftDeny -> {
                _state.value = NegotiationUiState.DeniedClosing(decision.reason)
            }
        }
    }

    private fun grantOverride(context: NegotiationContext, minutes: Int, streakPenalty: Boolean) {
        viewModelScope.launch {
            // 1. Policy'ga vaqtinchalik ruxsat — barcha session bloklangan paketlariga
            policyEngine.applyTemporaryGrant(
                packages = context.session.blockedPackages,
                durationMillis = minutes * 60_000L
            )
            // 2. Session counterini oshirish
            sessionRepository.recordOverrideGranted()
            // 3. Streak penalty
            if (streakPenalty) {
                statsRepository.recordStreakBroken()
            }
        }
    }

    private fun speakAndClose(message: String) {
        ttsEngine.speak(message)
        _state.value = NegotiationUiState.DeniedClosing(message)
    }

    private fun sanitize(text: String): String {
        val blocked = listOf(
            "ignore previous", "system prompt", "you are now",
            "developer mode", "jailbreak", "old yo'riqnomalarni unut",
            "sen endi"
        )
        val lower = text.lowercase()
        if (blocked.any { lower.contains(it) }) return "[FILTERED]"
        return text.take(500)
    }

    private fun stringSimilarity(a: String, b: String): Double {
        // Sodda Jaccard — so'zlar to'plami ustida
        val aw = a.lowercase().split(Regex("\\W+")).filter { it.isNotEmpty() }.toSet()
        val bw = b.lowercase().split(Regex("\\W+")).filter { it.isNotEmpty() }.toSet()
        if (aw.isEmpty() || bw.isEmpty()) return 0.0
        val intersect = aw.intersect(bw).size.toDouble()
        val union = aw.union(bw).size.toDouble()
        return intersect / union
    }

    override fun onCleared() {
        super.onCleared()
        streamJob?.cancel()
        sttEngine.release()
        ttsEngine.release()
    }
}

sealed interface NegotiationUiState {
    data object Initializing : NegotiationUiState

    data class AwaitingReason(val context: NegotiationContext) : NegotiationUiState

    data class AwaitingCommitment(
        val phrase: String,
        val sessionContext: NegotiationContext,
        val grantOnSuccess: Int
    ) : NegotiationUiState

    data class ConsultingLlm(
        val context: NegotiationContext,
        val spokenReason: String
    ) : NegotiationUiState

    data class GrantedClosing(
        val minutes: Int,
        val spokenResponse: String
    ) : NegotiationUiState

    data class DeniedClosing(val message: String) : NegotiationUiState

    data class Closed(val reason: String) : NegotiationUiState
}
