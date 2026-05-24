package uz.mentorai.focus.ui.onboarding.screens

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import uz.mentorai.focus.ui.components.MentorPrimaryButton
import uz.mentorai.focus.ui.components.MentorScreenScaffold
import uz.mentorai.focus.ui.components.MentorSectionLabel
import uz.mentorai.focus.ui.onboarding.OnboardingViewModel
import uz.mentorai.focus.ui.theme.MentorColors
import java.io.File

@Composable
fun VoiceCommitmentScreen(
    viewModel: OnboardingViewModel,
    onContinue: () -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    var hasMicPermission by remember {
        mutableStateOf(
            androidx.core.content.ContextCompat.checkSelfPermission(
                context, Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val micPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasMicPermission = granted }

    var recorder by remember { mutableStateOf<MediaRecorder?>(null) }
    var isRecording by remember { mutableStateOf(false) }
    var seconds by remember { mutableStateOf(0) }
    var savedPath by remember { mutableStateOf(state.voiceCommitmentPath) }

    LaunchedEffect(isRecording) {
        if (isRecording) {
            seconds = 0
            while (isRecording && seconds < 30) {
                delay(1000L)
                seconds++
            }
            if (isRecording) stopRecording(recorder, savedPath) { isRecording = false }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            try { recorder?.release() } catch (_: Exception) {}
        }
    }

    MentorScreenScaffold(
        title = "Ovozingni yozib qoldir.",
        subtitle = "Maqsadingni baland ovozda ayt. Bu yozuv kelajakda zaif lahzada o'zingga eshittiriladi.",
        bottomActions = {
            MentorPrimaryButton(
                text = if (savedPath != null) "Davom etish" else "Avval yozib ol",
                onClick = onContinue,
                enabled = savedPath != null
            )
        }
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            MentorSectionLabel(
                text = if (isRecording) "Yozyapti" else if (savedPath != null) "Yozib olindi" else "Tayyormisan?",
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(160.dp)
            ) {
                IconButton(
                    onClick = {
                        when {
                            !hasMicPermission -> micPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            isRecording -> stopRecording(recorder, savedPath) { isRecording = false }
                            else -> {
                                val (newRecorder, path) = startRecording(context)
                                if (newRecorder != null && path != null) {
                                    recorder = newRecorder
                                    savedPath = path
                                    isRecording = true
                                    viewModel.saveVoicePath(path)
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isRecording -> MentorColors.SignalFail
                                savedPath != null -> MentorColors.SignalCommit
                                else -> MentorColors.SurfaceIron
                            }
                        ),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MentorColors.TextPrimary
                    )
                ) {
                    Icon(
                        imageVector = when {
                            isRecording -> Icons.Default.Stop
                            savedPath != null -> Icons.Default.CheckCircle
                            else -> Icons.Default.Mic
                        },
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                text = when {
                    isRecording -> "%02d s".format(seconds)
                    savedPath != null -> "Yozildi. Qayta yozish uchun bos."
                    !hasMicPermission -> "Mikrofon ruxsati kerak"
                    else -> "Ayt: \"Men ${state.statedGoal.take(60)}...\""
                },
                color = MentorColors.TextBody,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
            )
        }
    }
}

private fun startRecording(context: android.content.Context): Pair<MediaRecorder?, String?> {
    return try {
        val file = File(context.filesDir, "commitment_${System.currentTimeMillis()}.m4a")
        val recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION") MediaRecorder()
        }
        recorder.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(file.absolutePath)
            prepare()
            start()
        }
        recorder to file.absolutePath
    } catch (e: Exception) {
        null to null
    }
}

private fun stopRecording(
    recorder: MediaRecorder?,
    path: String?,
    onStopped: () -> Unit
) {
    try {
        recorder?.stop()
        recorder?.release()
    } catch (_: Exception) {}
    onStopped()
}
