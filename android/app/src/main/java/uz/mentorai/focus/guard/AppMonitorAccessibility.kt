package uz.mentorai.focus.guard

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import uz.mentorai.focus.agent.voice.TtsEngine
import uz.mentorai.focus.data.onboarding.OnboardingRepository
import uz.mentorai.focus.data.session.SessionEntity
import uz.mentorai.focus.data.session.SessionRepository
import uz.mentorai.focus.guard.policy.PolicyEngine
import uz.mentorai.focus.guard.policy.Verdict
import uz.mentorai.focus.overlay.FocusOverlayManager
import uz.mentorai.focus.ui.negotiation.NegotiationActivity
import javax.inject.Inject

/**
 * Foreground ilovani real-time aniqlovchi AccessibilityService.
 * <50ms ichida xabar beradi → PolicyEngine'ga so'rov → kerak bo'lsa Hard Lock + TTS.
 */
@AndroidEntryPoint
class AppMonitorAccessibility : AccessibilityService() {

    @Inject lateinit var policyEngine: PolicyEngine
    @Inject lateinit var overlayManager: FocusOverlayManager
    @Inject lateinit var sessionRepository: SessionRepository
    @Inject lateinit var onboardingRepository: OnboardingRepository
    @Inject lateinit var ttsEngine: TtsEngine

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var lastPackage: String? = null
    private var lastSpokenAtMs: Long = 0L

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return
        val pkg = event.packageName?.toString() ?: return
        if (pkg == lastPackage) return
        lastPackage = pkg

        when (val verdict = policyEngine.evaluate(pkg, packageName)) {
            Verdict.Allow -> {
                if (pkg == PolicyEngine.ANDROID_SETTINGS_PKG && policyEngine.isInActiveSession()) {
                    val title = event.text?.firstOrNull()?.toString()
                        ?: event.contentDescription?.toString()
                    when (val st = policyEngine.evaluateSettingsScreen(title)) {
                        is Verdict.BypassTrap -> handleBypassTrap(st)
                        else -> overlayManager.remove()
                    }
                } else {
                    overlayManager.remove()
                }
            }

            is Verdict.Block -> handleBlock(verdict)
            is Verdict.BypassTrap -> handleBypassTrap(verdict)
        }
    }

    private fun handleBlock(verdict: Verdict.Block) {
        scope.launch {
            sessionRepository.recordIntercept()

            val session = policyEngine.activeSession.value ?: return@launch
            val onboarding = onboardingRepository.state.first()
            val displayApps = session.blockedPackages
                .map { AppDisplayNames.lookup(it) }
                .distinct()
                .take(5)

            val userName = onboarding.statedGoal
                .split(Regex("\\s+"))
                .firstOrNull()?.take(8)
                ?: "USER"

            // Mentor ovozi — throttled
            val now = System.currentTimeMillis()
            if (now - lastSpokenAtMs > MIN_SPEAK_INTERVAL_MS) {
                ttsEngine.speak(verdict.mentorMessage)
                lastSpokenAtMs = now
            }

            overlayManager.show(
                verdict = verdict,
                session = session,
                blockedAppsDisplay = displayApps,
                userName = userName,
                onMicPress = { launchNegotiation() },
                onMicRelease = { /* NegotiationActivity STT'ni boshqaradi */ },
                onCancelTask = { launchNegotiation() }
            )
        }
    }

    private fun launchNegotiation() {
        overlayManager.remove()
        ttsEngine.stop()
        val negotiationIntent = Intent(this, NegotiationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        startActivity(negotiationIntent)
    }

    private fun handleBypassTrap(verdict: Verdict.BypassTrap) {
        ttsEngine.speak(verdict.mentorMessage)
        overlayManager.showBypassTrap(
            verdict = verdict,
            onReturn = {
                overlayManager.remove()
                ttsEngine.stop()
                overlayManager.routeToHome()
            }
        )
    }

    override fun onInterrupt() = Unit

    override fun onServiceConnected() {
        super.onServiceConnected()
        serviceInfo = serviceInfo.apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS or
                    AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
            notificationTimeout = 50
        }
        ttsEngine.init()
        scope.launch { sessionRepository.loadActiveIntoEngine() }
    }

    override fun onDestroy() {
        scope.cancel()
        overlayManager.remove()
        ttsEngine.release()
        super.onDestroy()
    }

    companion object {
        private const val MIN_SPEAK_INTERVAL_MS = 8_000L
    }
}

/**
 * Paket nomidan brand display nomiga konvert.
 */
private object AppDisplayNames {
    private val map = mapOf(
        "com.instagram.android" to "INSTAGRAM",
        "com.zhiliaoapp.musically" to "TIKTOK",
        "com.google.android.youtube" to "YOUTUBE",
        "com.snapchat.android" to "SNAPCHAT",
        "com.facebook.katana" to "FACEBOOK",
        "com.twitter.android" to "X",
        "com.reddit.frontpage" to "REDDIT",
        "org.telegram.messenger" to "TELEGRAM",
        "com.whatsapp" to "WHATSAPP",
        "com.netflix.mediaclient" to "NETFLIX",
        "com.spotify.music" to "SPOTIFY",
        "com.king.candycrushsaga" to "CANDY CRUSH"
    )

    fun lookup(packageName: String): String =
        map[packageName] ?: packageName
            .substringAfterLast('.')
            .replace('_', ' ')
            .uppercase()
}
