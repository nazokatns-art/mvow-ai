package uz.mentorai.focus.ui.preview

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import uz.mentorai.focus.ui.routine.RoutineScreen
import uz.mentorai.focus.ui.theme.MentorTheme

/**
 * Routine (AI rejalashtirgich) preview activity'si.
 * docs/v2/preview/routine.html ga mos.
 */
@AndroidEntryPoint
class RoutinePreviewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MentorTheme {
                RoutineScreen(
                    onBack = { finish() },
                    onPlanReady = { energy, time, goals, habits, scope ->
                        Toast.makeText(
                            this,
                            "Reja · ${goals.joinToString()} · ${habits.size} odat · $scope",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }
                )
            }
        }
    }
}
