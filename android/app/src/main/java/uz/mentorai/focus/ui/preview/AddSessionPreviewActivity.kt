package uz.mentorai.focus.ui.preview

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import uz.mentorai.focus.ui.addsession.AddSessionScreen
import uz.mentorai.focus.ui.theme.MentorTheme

/**
 * Qo'lda sessiya qo'shish (add-session) preview activity'si.
 * docs/v2/preview/add-session.html ga mos.
 */
@AndroidEntryPoint
class AddSessionPreviewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MentorTheme {
                AddSessionScreen(
                    initialTitle = "Qur'on darsi",
                    initialGoal = "Bugun yana bir bobni o'zlashtirish",
                    initialTime = "14:30 — 15:15 · har kuni",
                    onSave = { title, goal, time, sev, type ->
                        Toast.makeText(this, "Muhrlandi: $title · $sev · $type", Toast.LENGTH_SHORT).show()
                        finish()
                    },
                    onCancel = { finish() }
                )
            }
        }
    }
}
