package uz.mentorai.focus.ui.preview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import uz.mentorai.focus.ui.theme.MentorTheme
import uz.mentorai.focus.ui.weekly.WeeklyReviewScreen

/**
 * Haftalik analitik tahlil (weekly-review) preview activity'si.
 * docs/v2/preview/weekly-review.html ga mos.
 */
@AndroidEntryPoint
class WeeklyReviewPreviewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MentorTheme {
                WeeklyReviewScreen(
                    onNextWeek = { finish() },
                    onClose = { finish() }
                )
            }
        }
    }
}
