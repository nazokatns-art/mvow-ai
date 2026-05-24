package uz.mentorai.focus.ui.preview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import uz.mentorai.focus.ui.chat.ChatScreen
import uz.mentorai.focus.ui.theme.MentorTheme

/**
 * Mentor bilan suhbat (chat) preview activity'si.
 * docs/v2/preview/chat.html ga mos.
 */
@AndroidEntryPoint
class ChatPreviewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MentorTheme {
                ChatScreen(onClose = { finish() })
            }
        }
    }
}
