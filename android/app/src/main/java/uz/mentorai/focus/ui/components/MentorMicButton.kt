package uz.mentorai.focus.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import uz.mentorai.focus.ui.theme.MentorColors

/**
 * Hard Lock'ning markaziy elementi — push-to-talk mic tugmasi.
 *
 * - 80dp asosiy doiracha (cyan)
 * - 32dp blur halo tashqi tomondan
 * - Hold gesture: ushlab turilganda + bosib turilganda glow oshadi
 * - Bosish va qo'yib yuborish — onPress / onRelease
 */
@Composable
fun MentorMicButton(
    onPress: () -> Unit,
    onRelease: () -> Unit,
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
    color: Color = MentorColors.AccentGlow
) {
    val infinite = rememberInfiniteTransition(label = "mic_pulse")
    val haloAlpha by infinite.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "halo"
    )

    Box(
        modifier = modifier.size(112.dp),
        contentAlignment = Alignment.Center
    ) {
        // Tashqi blur halo
        Box(
            modifier = Modifier
                .size(if (isActive) 110.dp else 96.dp)
                .blur(24.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = if (isActive) 0.7f else haloAlpha))
        )

        // Asosiy mic doiracha
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(color)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            onPress()
                            tryAwaitRelease()
                            onRelease()
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = "Hold to talk",
                tint = MentorColors.SurfaceVoid,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
