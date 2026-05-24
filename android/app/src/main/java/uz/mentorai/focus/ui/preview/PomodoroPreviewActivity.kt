package uz.mentorai.focus.ui.preview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import uz.mentorai.focus.ui.pomodoro.PomodoroScreen
import uz.mentorai.focus.ui.theme.MentorTheme

/**
 * Pomodoro (25/5/15 sikl) preview activity'si.
 * docs/v2/preview/pomodoro.html ga mos.
 */
@AndroidEntryPoint
class PomodoroPreviewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MentorTheme {
                PomodoroScreen(onSessionDone = { finish() })
            }
        }
    }
}
