package uz.mentorai.focus.ui.preview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import uz.mentorai.focus.overlay.HardBlockContent
import uz.mentorai.focus.ui.theme.MentorTheme

/**
 * Hard Lock screen'ning preview activity'si.
 *
 * Foydalanuvchi haqiqiy sessiya bo'lmasdan dizaynni ko'ra olishi uchun.
 * Mock ma'lumotlar bilan ishlaydi — 45 daqiqalik "Qur'on darsi" sessiyasi.
 *
 * Production'da ishlatilmaydi — faqat dev/preview rejimi.
 */
@AndroidEntryPoint
class HardLockPreviewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MentorTheme {
                val now = System.currentTimeMillis()
                // Mock: 45 daqiqalik sessiya, 10 daqiqa o'tdi (35 daqiqa qoldi)
                val startedAt = now - 10 * 60_000L
                val plannedEndAt = now + 35 * 60_000L

                HardBlockContent(
                    taskTitle = "QUR'ON DARSI",
                    sessionStartedAtMs = startedAt,
                    sessionPlannedEndAtMs = plannedEndAt,
                    blockedApps = listOf("INSTAGRAM", "TIKTOK", "YOUTUBE"),
                    userName = "AZIZ",
                    onMicPress = { /* preview — no-op */ },
                    onMicRelease = { /* preview — no-op */ },
                    onCancelTask = { finish() }
                )
            }
        }
    }
}
