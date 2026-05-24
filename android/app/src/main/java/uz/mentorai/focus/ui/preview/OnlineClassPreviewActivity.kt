package uz.mentorai.focus.ui.preview

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import uz.mentorai.focus.ui.online.OnlineClassScreen
import uz.mentorai.focus.ui.theme.MentorTheme

/**
 * Onlayn dars (online-class) preview activity'si.
 * docs/v2/preview/online-class.html ga mos.
 */
@AndroidEntryPoint
class OnlineClassPreviewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MentorTheme {
                OnlineClassScreen(
                    onLaunch = { mode ->
                        Toast.makeText(this, "${mode.modeName} ochilmoqda", Toast.LENGTH_SHORT).show()
                    },
                    onEnd = { finish() }
                )
            }
        }
    }
}
