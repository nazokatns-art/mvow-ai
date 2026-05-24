package uz.mentorai.focus.ui.preview

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import uz.mentorai.focus.ui.morning.MorningConfirmScreen
import uz.mentorai.focus.ui.theme.MentorTheme

/**
 * Tonggi tasdiq ekranining preview activity'si.
 * docs/v2/preview/morning-confirm.html ga mos.
 * Foydalanuvchi haqiqiy plan bo'lmasdan dizaynni ko'ra oladi.
 */
@AndroidEntryPoint
class MorningConfirmPreviewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MentorTheme {
                MorningConfirmScreen(
                    onConfirm = { changes, pickedIdx, energy ->
                        Toast.makeText(
                            this,
                            "Tasdiqlandi · o'zg: $changes · muhim: #$pickedIdx · energiya: $energy",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    },
                    onReopenPlan = { finish() }
                )
            }
        }
    }
}
