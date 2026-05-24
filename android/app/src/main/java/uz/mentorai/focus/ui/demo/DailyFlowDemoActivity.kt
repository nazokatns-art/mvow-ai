package uz.mentorai.focus.ui.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import dagger.hilt.android.AndroidEntryPoint
import uz.mentorai.focus.ui.brief.DailyBriefScreen
import uz.mentorai.focus.ui.celebrate.CelebrateScreen
import uz.mentorai.focus.ui.celebrate.CelebrateVariant
import uz.mentorai.focus.ui.dayflow.DayFlowScreen
import uz.mentorai.focus.ui.evening.TomorrowPlanScreen
import uz.mentorai.focus.ui.morning.MorningConfirmScreen
import uz.mentorai.focus.ui.reflection.SessionReflectionScreen
import uz.mentorai.focus.ui.routine.RoutineScreen
import uz.mentorai.focus.ui.theme.MentorTheme

/**
 * End-to-end demo flow — kun zanjirini bir butun qilib ko'rsatadi:
 * Routine (reja qurish) → Tomorrow plan (kechki) → Daily brief (tonggi savol) →
 * Morning confirm (tasdiq) → Day flow (taymer) → Session reflection (yakun) →
 * Weekly celebrate (bayram).
 *
 * Foydalanuvchi ilovaning to'liq qiymatini bir o'tirishda ko'ra oladi.
 */
@AndroidEntryPoint
class DailyFlowDemoActivity : ComponentActivity() {

    private enum class Phase {
        ROUTINE,         // 1. Bugungi rejani qurish (AI bilan)
        TOMORROW_PLAN,   // 2. Kechqurun: ertangi reja
        DAILY_BRIEF,     // 3. Tongda: 3 ta sokratik savol
        MORNING_CONFIRM, // 4. Tonggi tasdiqlash
        DAY_FLOW,        // 5. Kun davomida: taymer + holatlar
        SESSION_REFLECT, // 6. Har ish tugaganidan keyin baho
        CELEBRATE        // 7. Hafta oxiri: bayram
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MentorTheme {
                DemoFlow(onFinish = { finish() })
            }
        }
    }

    @Composable
    private fun DemoFlow(onFinish: () -> Unit) {
        var phase by remember { mutableStateOf(Phase.ROUTINE) }

        when (phase) {
            Phase.ROUTINE -> RoutineScreen(
                onBack = onFinish,
                onPlanReady = { _, _, _, _, _ -> phase = Phase.TOMORROW_PLAN }
            )

            Phase.TOMORROW_PLAN -> TomorrowPlanScreen(
                onSeal = { phase = Phase.DAILY_BRIEF },
                onSkipToMorning = { phase = Phase.DAILY_BRIEF }
            )

            Phase.DAILY_BRIEF -> DailyBriefScreen(
                onDone = { _, _, _ -> phase = Phase.MORNING_CONFIRM }
            )

            Phase.MORNING_CONFIRM -> MorningConfirmScreen(
                onConfirm = { _, _, _ -> phase = Phase.DAY_FLOW },
                onReopenPlan = { phase = Phase.TOMORROW_PLAN }
            )

            Phase.DAY_FLOW -> DayFlowScreen(
                onAllTasksDone = { phase = Phase.SESSION_REFLECT }
            )

            Phase.SESSION_REFLECT -> SessionReflectionScreen(
                onDone = { _, _ -> phase = Phase.CELEBRATE }
            )

            Phase.CELEBRATE -> CelebrateScreen(
                initial = CelebrateVariant.MOUNTAIN,
                onNextWeek = { onFinish() }
            )
        }
    }
}
