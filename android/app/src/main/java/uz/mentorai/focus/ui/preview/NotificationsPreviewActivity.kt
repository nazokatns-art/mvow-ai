package uz.mentorai.focus.ui.preview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import uz.mentorai.focus.ui.notifications.NotificationsScreen
import uz.mentorai.focus.ui.theme.MentorTheme

/**
 * Bildirishnomalar markazi (notifications) preview activity'si.
 * docs/v2/preview/notifications.html ga mos.
 */
@AndroidEntryPoint
class NotificationsPreviewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MentorTheme {
                NotificationsScreen(onClose = { finish() })
            }
        }
    }
}
