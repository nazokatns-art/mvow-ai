package uz.mentorai.focus.overlay

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.provider.Settings
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import dagger.hilt.android.qualifiers.ApplicationContext
import uz.mentorai.focus.data.language.LanguageRepository
import uz.mentorai.focus.data.session.SessionEntity
import uz.mentorai.focus.guard.policy.Verdict
import uz.mentorai.focus.ui.theme.MentorTheme
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Overlay'ni WindowManager orqali boshqaradi.
 * Idempotent show() — bir vaqtning o'zida bittadan ortiq overlay yo'q.
 */
@Singleton
class FocusOverlayManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val languageRepository: LanguageRepository
) {

    private val windowManager: WindowManager =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    private var composeView: ComposeView? = null
    private var lifecycleOwner: OverlayLifecycleOwner? = null
    private var currentBlockedPackage: String? = null

    fun canShow(): Boolean = Settings.canDrawOverlays(context)

    /**
     * Bloklangan ilova ustiga Hard Lock screen ko'tarish.
     * Mockup'ga muvofiq: full-screen + cyan timer + task pill + mic.
     */
    fun show(
        verdict: Verdict.Block,
        session: SessionEntity,
        blockedAppsDisplay: List<String>,
        userName: String,
        onMicPress: () -> Unit,
        onMicRelease: () -> Unit,
        onCancelTask: () -> Unit
    ) {
        if (!canShow()) return

        // Bir xil paketga ikkinchi marta — yangilamaymiz
        if (composeView != null && currentBlockedPackage == verdict.blockedPackage) return
        if (composeView != null) remove()

        val owner = OverlayLifecycleOwner().apply { onCreate(); onStart() }
        val view = ComposeView(context).apply {
            owner.attachTo(this)
            setContent {
                MentorTheme {
                    HardBlockContent(
                        taskTitle = session.title.ifBlank { session.goal.take(40) },
                        sessionStartedAtMs = session.startedAtMillis,
                        sessionPlannedEndAtMs = session.plannedEndAtMillis,
                        blockedApps = blockedAppsDisplay,
                        userName = userName,
                        onMicPress = onMicPress,
                        onMicRelease = onMicRelease,
                        onCancelTask = onCancelTask
                    )
                }
            }
        }

        windowManager.addView(view, layoutParams())
        composeView = view
        lifecycleOwner = owner
        currentBlockedPackage = verdict.blockedPackage
    }

    fun showBypassTrap(verdict: Verdict.BypassTrap, onReturn: () -> Unit) {
        if (!canShow()) return
        if (composeView != null) remove()

        val owner = OverlayLifecycleOwner().apply { onCreate(); onStart() }
        val view = ComposeView(context).apply {
            owner.attachTo(this)
            setContent {
                MentorTheme {
                    BypassTrapContent(
                        message = verdict.mentorMessage,
                        onReturn = onReturn
                    )
                }
            }
        }

        windowManager.addView(view, layoutParams())
        composeView = view
        lifecycleOwner = owner
        currentBlockedPackage = "__bypass_trap__"
    }

    fun remove() {
        composeView?.let {
            try { windowManager.removeViewImmediate(it) } catch (_: Exception) {}
        }
        lifecycleOwner?.onDestroy()
        composeView = null
        lifecycleOwner = null
        currentBlockedPackage = null
    }

    fun routeToHome() {
        val home = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(home)
    }

    private fun layoutParams(): WindowManager.LayoutParams {
        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams.TYPE_PHONE
        }
        return WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            type,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
            }
        }
    }
}
