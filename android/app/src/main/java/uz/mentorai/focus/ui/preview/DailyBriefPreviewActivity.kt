package uz.mentorai.focus.ui.preview

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import uz.mentorai.focus.ui.brief.DailyBriefScreen
import uz.mentorai.focus.ui.theme.MentorTheme

/**
 * Tonggi brifing (daily-brief) preview activity'si.
 * docs/v2/preview/daily-brief.html ga mos.
 */
@AndroidEntryPoint
class DailyBriefPreviewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MentorTheme {
                DailyBriefScreen(
                    onDone = { identity, priority, tinyStep ->
                        Toast.makeText(
                            this,
                            "Brifing yakunlandi · $identity → $priority → $tinyStep",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                )
            }
        }
    }
}
