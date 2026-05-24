package uz.mentorai.focus.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.mentorai.focus.ui.theme.MentorColors

/**
 * Cyan gradient pill — joriy vazifa nomi.
 * Mockup'dagi "QUR'ON DARSI" pill.
 */
@Composable
fun TaskPill(
    title: String,
    modifier: Modifier = Modifier
) {
    val gradient = Brush.linearGradient(
        colors = listOf(MentorColors.AccentGlow, MentorColors.AccentGlowEnd)
    )

    Text(
        text = title.uppercase(),
        color = MentorColors.SurfaceVoid,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 3.sp,
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(gradient)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    )
}
