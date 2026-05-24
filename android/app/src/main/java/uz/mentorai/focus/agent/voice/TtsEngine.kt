package uz.mentorai.focus.agent.voice

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.mentorai.focus.data.language.Language
import uz.mentorai.focus.data.language.LanguageRepository
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Android TextToSpeech wrapper. Foydalanuvchi tilida gapiradi.
 *
 * Mentor ovozi: chuqurroq pitch (0.85), sekinroq tezlik (0.9).
 */
@Singleton
class TtsEngine @Inject constructor(
    @ApplicationContext private val context: Context,
    private val languageRepository: LanguageRepository
) {
    private var tts: TextToSpeech? = null
    private val readyDeferred = CompletableDeferred<Boolean>()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val streamBuffer = StringBuilder()
    private var streamUtteranceCounter = 0

    fun init() {
        if (tts != null) return
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                applyLanguage(languageRepository.current.value)
                tts?.apply {
                    setPitch(0.85f)
                    setSpeechRate(0.9f)
                    setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                        override fun onStart(utteranceId: String?) { _isSpeaking.value = true }
                        override fun onDone(utteranceId: String?) { _isSpeaking.value = false }
                        @Deprecated("Deprecated in Java")
                        override fun onError(utteranceId: String?) { _isSpeaking.value = false }
                        override fun onError(utteranceId: String?, errorCode: Int) {
                            _isSpeaking.value = false
                        }
                    })
                }
                readyDeferred.complete(true)

                // Til o'zgarishlarini kuzatib turish
                scope.launch {
                    languageRepository.current.collect { applyLanguage(it) }
                }
            } else {
                readyDeferred.complete(false)
            }
        }
    }

    private fun applyLanguage(language: Language) {
        val tts = tts ?: return
        val attemptOrder = listOf(language.ttsLocale, Locale.ENGLISH)
        for (locale in attemptOrder) {
            val result = tts.setLanguage(locale)
            if (result != TextToSpeech.LANG_MISSING_DATA &&
                result != TextToSpeech.LANG_NOT_SUPPORTED
            ) return
        }
    }

    suspend fun awaitReady(): Boolean = readyDeferred.await()

    fun speak(text: String, queueAdd: Boolean = false) {
        val mode = if (queueAdd) TextToSpeech.QUEUE_ADD else TextToSpeech.QUEUE_FLUSH
        tts?.speak(text, mode, null, "mentor_${System.currentTimeMillis()}")
    }

    fun appendStreamToken(token: String) {
        streamBuffer.append(token)
        val sentenceEnd = lastSentenceEnd(streamBuffer)
        if (sentenceEnd > 0) {
            val toSpeak = streamBuffer.substring(0, sentenceEnd).trim()
            if (toSpeak.isNotEmpty()) {
                tts?.speak(
                    toSpeak,
                    TextToSpeech.QUEUE_ADD,
                    null,
                    "mentor_stream_${streamUtteranceCounter++}"
                )
            }
            streamBuffer.delete(0, sentenceEnd)
        }
    }

    fun finishStream() {
        val remainder = streamBuffer.toString().trim()
        if (remainder.isNotEmpty()) {
            tts?.speak(
                remainder,
                TextToSpeech.QUEUE_ADD,
                null,
                "mentor_stream_${streamUtteranceCounter++}"
            )
        }
        streamBuffer.clear()
    }

    fun stop() {
        tts?.stop()
        streamBuffer.clear()
        _isSpeaking.value = false
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        streamBuffer.clear()
    }

    private fun lastSentenceEnd(sb: StringBuilder): Int {
        for (i in sb.length - 1 downTo 0) {
            if (sb[i] == '.' || sb[i] == '!' || sb[i] == '?' || sb[i] == '\n') {
                return i + 1
            }
        }
        return 0
    }
}
