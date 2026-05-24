package uz.mentorai.focus.guard

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import uz.mentorai.focus.data.session.SessionEntity
import uz.mentorai.focus.data.session.SessionRepository
import javax.inject.Inject

/**
 * Reboot'dan keyin guard service va planlangan sessiyalarni tiklaydi.
 */
@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject lateinit var sessionRepository: SessionRepository

    override fun onReceive(context: Context, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_LOCKED_BOOT_COMPLETED -> {
                val pendingResult = goAsync()
                CoroutineScope(SupervisorJob() + Dispatchers.IO).launch {
                    try {
                        sessionRepository.loadActiveIntoEngine()
                        // Agar faol sessiya bo'lsa va vaqti tugamagan bo'lsa — service'ni qayta ishga tushirish
                        FocusGuardService.start(context)
                    } finally {
                        pendingResult.finish()
                    }
                }
            }
        }
    }
}
