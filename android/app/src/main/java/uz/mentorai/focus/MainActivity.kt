package uz.mentorai.focus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import dagger.hilt.android.AndroidEntryPoint
import uz.mentorai.focus.ui.theme.MentorColors
import uz.mentorai.focus.ui.theme.MentorTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val voidColor = MentorColors.SurfaceVoid.toArgb()
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(voidColor),
            navigationBarStyle = SystemBarStyle.dark(voidColor)
        )
        super.onCreate(savedInstanceState)
        setContent {
            MentorTheme {
                MentorApp()
            }
        }
    }
}
