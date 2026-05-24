package uz.mentorai.focus.ui.session

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import uz.mentorai.focus.data.scheduled.ScheduledSessionEntity
import uz.mentorai.focus.ui.components.MentorGhostButton
import uz.mentorai.focus.ui.components.MentorPrimaryButton
import uz.mentorai.focus.ui.theme.MentorColors
import uz.mentorai.focus.ui.theme.MentorTheme

/**
 * Lock screen ustida ochiladigan Activity.
 * Foydalanuvchi BOSHLAYMAN yoki hozirmas tugmasini bosishi kerak.
 */
@AndroidEntryPoint
class SessionStartActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )
        }

        val sessionId = intent.getStringExtra(EXTRA_SESSION_ID)
        if (sessionId == null) {
            finish()
            return
        }

        setContent {
            MentorTheme {
                SessionStartRoot(
                    sessionId = sessionId,
                    onClose = { finish() }
                )
            }
        }
    }

    companion object {
        const val EXTRA_SESSION_ID = "session_id"
    }
}

@Composable
private fun SessionStartRoot(
    sessionId: String,
    onClose: () -> Unit,
    viewModel: SessionStartViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(sessionId) { viewModel.load(sessionId) }

    when (val s = state) {
        SessionStartUiState.Loading -> LoadingScreen()
        SessionStartUiState.NotFound -> {
            LaunchedEffect(Unit) { onClose() }
        }
        is SessionStartUiState.Ready -> {
            SessionStartContent(
                state = s,
                onStart = { viewModel.startNow(sessionId, onClose) },
                onPostpone = { minutes ->
                    viewModel.postpone(sessionId, minutes, onClose)
                }
            )
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MentorColors.SurfaceVoid)
    )
}

@Composable
private fun SessionStartContent(
    state: SessionStartUiState.Ready,
    onStart: () -> Unit,
    onPostpone: (Int) -> Unit
) {
    val scheduled = state.scheduled

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MentorColors.SurfaceVoid)
            .padding(24.dp)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = severityLabel(scheduled.severity),
                color = MentorColors.AccentBrass,
                fontSize = 11.sp,
                letterSpacing = 4.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(40.dp))

            Text(
                text = "VAQT KELDI",
                color = MentorColors.TextPrimary,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = scheduled.title,
                color = MentorColors.TextBody,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "\"${scheduled.goal}\"",
                color = MentorColors.TextMuted,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            )

            if (scheduled.postponementsToday > 0) {
                Spacer(Modifier.height(24.dp))
                Text(
                    text = "Bugun ${scheduled.postponementsToday} marta orqaga surding",
                    color = MentorColors.SignalFail,
                    fontSize = 12.sp
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MentorPrimaryButton(
                text = "Boshlayman",
                onClick = onStart
            )
            if (state.canPostpone) {
                MentorGhostButton(
                    text = "5 daq orqaga surish",
                    onClick = { onPostpone(5) }
                )
            } else {
                Text(
                    text = "Bugun limit tugadi. Faqat boshlash mumkin.",
                    color = MentorColors.TextMuted,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

private fun severityLabel(severity: String): String = when (severity) {
    ScheduledSessionEntity.SEVERITY_MAX -> "MUQADDAS VAQT"
    ScheduledSessionEntity.SEVERITY_HIGH -> "YUQORI USTUVORLIK"
    ScheduledSessionEntity.SEVERITY_MEDIUM -> "REJADAGI"
    ScheduledSessionEntity.SEVERITY_LOW -> "OPTSIYONAL"
    else -> "REJADAGI"
}
