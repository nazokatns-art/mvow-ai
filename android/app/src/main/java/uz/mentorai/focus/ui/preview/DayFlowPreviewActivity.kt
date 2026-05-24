package uz.mentorai.focus.ui.preview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import uz.mentorai.focus.ui.dayflow.DayFlowScreen
import uz.mentorai.focus.ui.theme.MentorTheme

/**
 * Kunlik oqim (day-flow) preview activity'si.
 * docs/v2/preview/day-flow.html ga mos.
 */
@AndroidEntryPoint
class DayFlowPreviewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MentorTheme {
                DayFlowScreen(onAllTasksDone = { finish() })
            }
        }
    }
}
