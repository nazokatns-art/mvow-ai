package uz.mentorai.focus.ui.preview

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import uz.mentorai.focus.ui.evening.TomorrowPlanScreen
import uz.mentorai.focus.ui.theme.MentorTheme

/**
 * Ertangi reja ekranining preview activity'si.
 * docs/v2/preview/tomorrow-plan.html ga mos.
 */
@AndroidEntryPoint
class TomorrowPlanPreviewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MentorTheme {
                TomorrowPlanScreen(
                    onSeal = { tasks ->
                        Toast.makeText(
                            this,
                            "Reja muhrlandi · ${tasks.size} ta ish",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    },
                    onSkipToMorning = { finish() }
                )
            }
        }
    }
}
