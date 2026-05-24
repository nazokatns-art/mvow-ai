package uz.mentorai.focus.ui.preview

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import uz.mentorai.focus.ui.reflection.SessionReflectionScreen
import uz.mentorai.focus.ui.theme.MentorTheme

/**
 * Sessiya bahosi (session-reflection) preview activity'si.
 * docs/v2/preview/session-reflection.html ga mos.
 */
@AndroidEntryPoint
class SessionReflectionPreviewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MentorTheme {
                SessionReflectionScreen(
                    onDone = { knowledge, strength ->
                        Toast.makeText(
                            this,
                            "Sessiya bahosi · bilim: $knowledge · kuch: $strength",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                )
            }
        }
    }
}
