package uz.mentorai.focus.guard

import android.app.admin.DeviceAdminReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent

/**
 * DeviceAdmin receiver — uninstall'ga qarshi qo'shimcha qatlam.
 *
 * Sprint 4'da to'liq foydalanish:
 *  - setUninstallBlocked(true)
 *  - lockNow() — ekranni qulflash (extreme tier)
 *  - setPasswordQuality()
 */
class MentorDeviceAdmin : DeviceAdminReceiver() {

    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
        // TODO(sprint-4): persist enabled state, notify FSM
    }

    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
        // TODO(sprint-4): treat as bypass attempt, trigger penalty
    }

    override fun onDisableRequested(context: Context, intent: Intent): CharSequence {
        // Foydalanuvchi DeviceAdmin'ni o'chirmoqchi — bu warning matn ko'rsatiladi.
        return "Mentor o'chirilsa, sening intizoming ham o'chadi. Bu — sen istagan natijami?"
    }

    companion object {
        fun componentName(context: Context): ComponentName =
            ComponentName(context, MentorDeviceAdmin::class.java)
    }
}
