package uz.mentorai.focus.ui.preview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import uz.mentorai.focus.ui.celebrate.CelebrateScreen
import uz.mentorai.focus.ui.celebrate.CelebrateVariant
import uz.mentorai.focus.ui.theme.MentorTheme
import java.util.Calendar

/**
 * Haftalik bayram (celebrate) preview activity'si.
 * docs/v2/preview/celebrate.html ga mos.
 * Haftalik avtomatik o'zgarib turadigan variant.
 */
@AndroidEntryPoint
class CelebratePreviewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MentorTheme {
                val weekIndex = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)
                val variants = CelebrateVariant.entries
                val initial = variants[weekIndex % variants.size]

                CelebrateScreen(
                    initial = initial,
                    onNextWeek = { finish() }
                )
            }
        }
    }
}
