package uz.mentorai.focus.overlay

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import uz.mentorai.focus.ui.components.BlockedDistractionsList
import uz.mentorai.focus.ui.components.BrandHeader
import uz.mentorai.focus.ui.components.CircularTimer
import uz.mentorai.focus.ui.components.MentorMicButton
import uz.mentorai.focus.ui.components.TaskPill
import uz.mentorai.focus.ui.theme.MentorColors

/**
 * Hard Lock screen — mockup'ga muvofiq pixel-perfect.
 *
 * Asosiy tarkib:
 *  1. M-VoW header
 *  2. "Hard Lock" badge
 *  3. CircularTimer (cyan glow)
 *  4. TaskPill (cyan gradient)
 *  5. Blocked distractions
 *  6. Mic button + actions
 *
 * Time'ni o'zi tick'laydi (har 1 soniyada), shunchaki start + end millisni qabul qiladi.
 */
@Composable
fun HardBlockContent(
    taskTitle: String,
    sessionStartedAtMs: Long,
    sessionPlannedEndAtMs: Long,
    blockedApps: List<String>,
    userName: String = "USER",
    onMicPress: () -> Unit = {},
    onMicRelease: () -> Unit = {},
    onCancelTask: () -> Unit = {}
) {
    val totalMs = (sessionPlannedEndAtMs - sessionStartedAtMs).coerceAtLeast(1L)

    var nowMs by remember { mutableLongStateOf(System.currentTimeMillis()) }
    LaunchedEffect(sessionStartedAtMs, sessionPlannedEndAtMs) {
        while (true) {
            nowMs = System.currentTimeMillis()
            delay(1000)
        }
    }
    val remainingMs = (sessionPlannedEndAtMs - nowMs).coerceAtLeast(0L)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MentorColors.SurfaceVoid)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BrandHeader()

            Spacer(modifier = Modifier.height(12.dp))

            HardLockBadge()

            Spacer(modifier = Modifier.height(40.dp))

            CircularTimer(
                remainingMs = remainingMs,
                totalMs = totalMs,
                label = "until next planned break"
            )

            Spacer(modifier = Modifier.height(40.dp))

            TaskPill(title = taskTitle)

            Spacer(modifier = Modifier.height(36.dp))

            BlockedDistractionsList(distractions = blockedApps)

            Spacer(modifier = Modifier.weight(1f))
        }

        BottomActionDrawer(
            userName = userName,
            onMicPress = onMicPress,
            onMicRelease = onMicRelease,
            onCancelTask = onCancelTask,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        )
    }
}

@Composable
private fun HardLockBadge() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(MentorColors.SurfaceShadow)
            .border(
                width = 1.dp,
                color = MentorColors.AccentGlowDim,
                shape = RoundedCornerShape(999.dp)
            )
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text(
            text = "HARD LOCK",
            color = MentorColors.AccentGlow,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 3.sp
        )
    }
}

@Composable
private fun BottomActionDrawer(
    userName: String,
    onMicPress: () -> Unit,
    onMicRelease: () -> Unit,
    onCancelTask: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MentorColors.SurfaceShadow)
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        MentorMicButton(
            onPress = onMicPress,
            onRelease = onMicRelease
        )
        Text(
            text = "HOLD TO COMMUNICATE",
            color = MentorColors.TextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 3.sp
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(4.dp))
                .clickable { onCancelTask() }
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "CANCEL TASK",
                color = MentorColors.TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 2.sp
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "FOCUS, ${userName.uppercase()}. NO DISTRACTIONS.",
            color = MentorColors.TextBody,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "STAY DISCIPLINED (TTS)",
            color = MentorColors.TextGhost,
            fontSize = 10.sp,
            letterSpacing = 2.sp,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Bypass-trap content — Settings'ga kirish urinishi paytida.
 */
@Composable
fun BypassTrapContent(
    message: String,
    onReturn: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MentorColors.SurfaceVoid)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            BrandHeader()
            Text(
                text = message,
                color = MentorColors.TextPrimary,
                fontSize = 20.sp,
                lineHeight = 30.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MentorColors.AccentGlow)
                .clickable { onReturn() }
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "RETURN",
                color = MentorColors.SurfaceVoid,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 3.sp,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }
    }
}
