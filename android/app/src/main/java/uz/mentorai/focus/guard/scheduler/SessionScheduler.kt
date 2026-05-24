package uz.mentorai.focus.guard.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import uz.mentorai.focus.data.scheduled.ScheduledSessionEntity
import uz.mentorai.focus.guard.SessionStartReceiver
import javax.inject.Inject
import javax.inject.Singleton

/**
 * `AlarmManager.setExactAndAllowWhileIdle` orqali sessiya boshlanish vaqtini rejalashtiradi.
 * Doze rejimida ham ishlaydi (IDLE bo'ladigan ham).
 */
@Singleton
class SessionScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    /** Aniq vaqtga rejalashtirish — Doze'ga qaramay */
    fun schedule(session: ScheduledSessionEntity) {
        if (session.startAtMillis <= System.currentTimeMillis()) return

        val pi = pendingIntentFor(session.id)

        // Android 12+ — exact alarm uchun ruxsat tekshiruvi
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            !alarmManager.canScheduleExactAlarms()
        ) {
            // Foydalanuvchi sozlamalarda yoqishi kerak — fallback inexact
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                session.startAtMillis,
                pi
            )
            return
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            session.startAtMillis,
            pi
        )
    }

    fun cancel(sessionId: String) {
        alarmManager.cancel(pendingIntentFor(sessionId))
    }

    fun rescheduleAll(sessions: List<ScheduledSessionEntity>) {
        sessions.forEach { schedule(it) }
    }

    private fun pendingIntentFor(sessionId: String): PendingIntent {
        val intent = Intent(context, SessionStartReceiver::class.java).apply {
            action = ACTION_TRIGGER
            putExtra(SessionStartReceiver.EXTRA_SESSION_ID, sessionId)
        }
        return PendingIntent.getBroadcast(
            context,
            sessionId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        const val ACTION_TRIGGER = "uz.mentorai.focus.action.TRIGGER_SESSION"
    }
}
