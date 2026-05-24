package uz.mentorai.focus.ui.preview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import uz.mentorai.focus.ui.profile.GrowthStage
import uz.mentorai.focus.ui.profile.HabitChip
import uz.mentorai.focus.ui.profile.ProfileScreen
import uz.mentorai.focus.ui.theme.MentorTheme

/**
 * Foydalanuvchi profili (profile) preview activity'si.
 * docs/v2/preview/profile.html ga mos.
 */
@AndroidEntryPoint
class ProfilePreviewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MentorTheme {
                ProfileScreen(
                    userName = "Aziz",
                    sinceLabel = "12 KUN YO'L · 2026-MAY-3 DAN",
                    becomingText = "O'z so'zida turadigan odam — bu sen bo'lyapsan.",
                    becomingEm = "O'z so'zida turadigan",
                    streakNow = 12,
                    bestStreak = 12,
                    totalSessions = 38,
                    totalHours = 47,
                    stage = GrowthStage.TREE,
                    habits = listOf(
                        HabitChip("☾", "Tonggi namoz", 12),
                        HabitChip("⊙", "Yugurish · 5 km", 9),
                        HabitChip("▥", "Mutolaa · 30 daq", 7)
                    ),
                    relationDepth = "CHUQUR TANISHUV",
                    relationText = "Sening ritmingni bilaman. Tongda kuchli, tushga charchaysan, kechqurun yana yumshaysan.",
                    relationEm = "Sening ritmingni",
                    onBack = { finish() },
                    onSettings = { finish() }
                )
            }
        }
    }
}
