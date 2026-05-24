package uz.mentorai.focus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.mentorai.focus.ui.theme.MentorColors

/**
 * Onboarding'da har bir ekran uchun standart layout.
 * Yuqorida — sarlavha. O'rtada — kontent. Pastda — harakat tugmalari.
 */
@Composable
fun MentorScreenScaffold(
    title: String,
    subtitle: String? = null,
    bottomActions: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MentorColors.SurfaceVoid)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(64.dp))
            Text(
                text = title,
                color = MentorColors.TextPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )
            if (subtitle != null) {
                Spacer(Modifier.height(12.dp))
                Text(
                    text = subtitle,
                    color = MentorColors.TextBody,
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )
            }
            Spacer(Modifier.height(40.dp))
            content()
            Spacer(Modifier.height(120.dp))
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(MentorColors.SurfaceVoid)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            bottomActions()
        }
    }
}
