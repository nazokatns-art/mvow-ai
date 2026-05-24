package uz.mentorai.focus.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.mentorai.focus.ui.theme.MentorColors

/**
 * M-VoW brand header — qalqon shakli + nom.
 *
 * Stylization: M-VoW (qattiq qoida — har doim shu shakl).
 */
@Composable
fun BrandHeader(
    modifier: Modifier = Modifier,
    subtitle: String? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        ShieldLogo(size = 18.dp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "M-VoW",
            color = MentorColors.TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 4.sp
        )
        if (subtitle != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "—",
                color = MentorColors.TextMuted,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = subtitle.uppercase(),
                color = MentorColors.AccentGlow,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 3.sp
            )
        }
    }
}

@Composable
private fun ShieldLogo(size: androidx.compose.ui.unit.Dp) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        val path = Path().apply {
            // Qalqon konturi
            moveTo(w * 0.5f, 0f)
            lineTo(w, h * 0.25f)
            lineTo(w, h * 0.65f)
            // Pastki nuqta
            cubicTo(
                w, h * 0.85f,
                w * 0.5f, h,
                w * 0.5f, h
            )
            cubicTo(
                w * 0.5f, h,
                0f, h * 0.85f,
                0f, h * 0.65f
            )
            lineTo(0f, h * 0.25f)
            close()
        }

        drawPath(
            path = path,
            color = MentorColors.AccentGlow,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = 1.5.dp.toPx())
        )

        // Markaz nuqta (V harfi indikatori)
        drawCircle(
            color = MentorColors.AccentGlow,
            radius = w * 0.08f,
            center = Offset(w * 0.5f, h * 0.5f)
        )
    }
}
