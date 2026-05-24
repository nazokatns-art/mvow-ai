package uz.mentorai.focus.guard

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import dagger.hilt.android.AndroidEntryPoint
import uz.mentorai.focus.ui.session.SessionStartActivity

/**
 * AlarmManager tomonidan triggerlanadi.
 * Telefonni majburan uyg'otib, SessionStartActivity'ni lock screen ustida ochadi.
 */
@AndroidEntryPoint
class SessionStartReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        val sessionId = intent?.getStringExtra(EXTRA_SESSION_ID) ?: return

        // 1. WakeLock — ekranni yoqish (FULL_WAKE_LOCK deprecated, ON_AFTER_RELEASE bilan)
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        @Suppress("DEPRECATION")
        val wakeLock = pm.newWakeLock(
            PowerManager.FULL_WAKE_LOCK or
            PowerManager.ACQUIRE_CAUSES_WAKEUP or
            PowerManager.ON_AFTER_RELEASE,
            "Mentor:SessionStart"
        )
        wakeLock.acquire(60_000L)  // Maksimum 60 soniya

        // 2. SessionStartActivity'ni lock screen ustida ochish
        val activityIntent = Intent(context, SessionStartActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
            putExtra(SessionStartActivity.EXTRA_SESSION_ID, sessionId)
        }
        context.startActivity(activityIntent)

        // 3. WakeLock'ni tezda release qilamiz — Activity setShowWhenLocked + setTurnScreenOn
        // o'zi ekranni yoqib qoladi.
        try { wakeLock.release() } catch (_: Exception) {}
    }

    companion object {
        const val EXTRA_SESSION_ID = "session_id"
    }
}
