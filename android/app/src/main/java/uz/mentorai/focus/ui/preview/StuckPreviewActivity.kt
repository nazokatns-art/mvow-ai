package uz.mentorai.focus.ui.preview

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import uz.mentorai.focus.ui.stuck.StuckScreen
import uz.mentorai.focus.ui.theme.MentorTheme

/**
 * Turg'unlik (stuck) preview activity'si.
 * docs/v2/preview/stuck.html ga mos.
 */
@AndroidEntryPoint
class StuckPreviewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MentorTheme {
                StuckScreen(
                    onResolve = { feeling, protecting, tinyStep ->
                        Toast.makeText(
                            this,
                            "Tuman ko'tarildi · $feeling → $protecting → $tinyStep",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    },
                    onDeferTalk = { finish() }
                )
            }
        }
    }
}
