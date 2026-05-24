package uz.mentorai.focus.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.mentorai.focus.ui.theme.MentorColors

/**
 * Mockup'dagi 240dp glow ringli taymer.
 *
 * Cyan progress arc + halo blur tashqi tomondan.
 * Markazda: 56sp mono digits.
 *
 * Format:
 *  remainingMs >= 1 soat → "HH:MM:SS"
 *  remainingMs < 1 soat → "MM:SS"
 */
@Composable
fun CircularTimer(
    remainingMs: Long,
    totalMs: Long,
    modifier: Modifier = Modifier,
    diameter: Dp = 240.dp,
    strokeWidth: Dp = 4.dp,
    glowColor: Color = MentorColors.AccentGlow,
    trackColor: Color = MentorColors.SurfaceIron,
    label: String? = null
) {
    val progress = remember(remainingMs, totalMs) {
        if (totalMs <= 0) 0f
        else (remainingMs.coerceAtLeast(0L).toFloat() / totalMs.toFloat()).coerceIn(0f, 1f)
    }

    Box(
        modifier = modifier.size(diameter),
        contentAlignment = Alignment.Center
    ) {
        // Tashqi halo (blur)
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .blur(24.dp)
        ) {
            drawArc(
                color = glowColor.copy(alpha = 0.35f),
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                topLeft = Offset(strokeWidth.toPx() / 2, strokeWidth.toPx() / 2),
                size = Size(
                    size.width - strokeWidth.toPx(),
                    size.height - strokeWidth.toPx()
                ),
                style = Stroke(
                    width = strokeWidth.toPx() * 2.5f,
                    cap = StrokeCap.Round
                )
            )
        }

        // Asosiy ring
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Track (background ring)
            drawArc(
                color = trackColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(strokeWidth.toPx() / 2, strokeWidth.toPx() / 2),
                size = Size(
                    size.width - strokeWidth.toPx(),
                    size.height - strokeWidth.toPx()
                ),
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
            // Progress arc
            drawArc(
                color = glowColor,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                topLeft = Offset(strokeWidth.toPx() / 2, strokeWidth.toPx() / 2),
                size = Size(
                    size.width - strokeWidth.toPx(),
                    size.height - strokeWidth.toPx()
                ),
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            )
        }

        // Markaz: time digits
        androidx.compose.foundation.layout.Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = formatRemaining(remainingMs),
                color = MentorColors.AccentGlow,
                fontSize = 48.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 2.sp
            )
            if (label != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = label.uppercase(),
                    color = MentorColors.TextMuted,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}

private fun formatRemaining(ms: Long): String {
    val safe = ms.coerceAtLeast(0L)
    val totalSeconds = safe / 1000L
    val hours = totalSeconds / 3600L
    val minutes = (totalSeconds % 3600L) / 60L
    val seconds = totalSeconds % 60L

    return if (hours > 0L) {
        "%02d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        "%02d:%02d".format(minutes, seconds)
    }
}
