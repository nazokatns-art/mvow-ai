package uz.mentorai.focus.ui.preview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import uz.mentorai.focus.ui.rest.RestModeKind
import uz.mentorai.focus.ui.rest.RestModeScreen
import uz.mentorai.focus.ui.theme.MentorTheme

/**
 * Dam rejimi (rest-mode) preview activity'si.
 * docs/v2/preview/rest-mode.html ga mos.
 */
@AndroidEntryPoint
class RestModePreviewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MentorTheme {
                RestModeScreen(
                    kind = RestModeKind.BURNOUT,
                    onAccept = { finish() },
                    onOverride = { finish() }
                )
            }
        }
    }
}
