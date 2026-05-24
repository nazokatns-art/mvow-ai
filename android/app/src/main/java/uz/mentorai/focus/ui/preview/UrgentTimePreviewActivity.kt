package uz.mentorai.focus.ui.preview

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import uz.mentorai.focus.ui.theme.MentorTheme
import uz.mentorai.focus.ui.urgent.UrgentTimeScreen

/**
 * Shoshilinch vaqt (urgent-time) preview activity'si.
 * docs/v2/preview/urgent-time.html ga mos.
 */
@AndroidEntryPoint
class UrgentTimePreviewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MentorTheme {
                UrgentTimeScreen(
                    onGrant = { minutes ->
                        Toast.makeText(
                            this,
                            "$minutes daqiqaga ruxsat berildi",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    },
                    onCancel = { finish() }
                )
            }
        }
    }
}
