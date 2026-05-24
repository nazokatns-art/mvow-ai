package uz.mentorai.focus.ui.negotiation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import uz.mentorai.focus.agent.voice.SttState
import uz.mentorai.focus.i18n.UiStrings
import uz.mentorai.focus.ui.LocalLanguage
import uz.mentorai.focus.ui.components.MentorPrimaryButton
import uz.mentorai.focus.ui.theme.MentorColors

@Composable
fun NegotiationScreen(
    viewModel: NegotiationViewModel,
    onClose: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val sttState by viewModel.sttEngine.state.collectAsState()
    val isSpeaking by viewModel.ttsEngine.isSpeaking.collectAsState()
    val lang = LocalLanguage.current

    LaunchedEffect(Unit) { viewModel.start() }

    LaunchedEffect(state) {
        when (state) {
            is NegotiationUiState.GrantedClosing,
            is NegotiationUiState.DeniedClosing,
            is NegotiationUiState.Closed -> {
                delay(3500)  // TTS gapirib bo'lguncha kutamiz
                onClose()
            }
            else -> Unit
        }
    }

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
            // Mentor "ko'zi"
            Box(
                modifier = Modifier
                    .size(if (isSpeaking) 96.dp else 64.dp)
                    .clip(CircleShape)
                    .background(MentorColors.AccentBrass)
            )
            Spacer(Modifier.height(48.dp))

            when (val s = state) {
                NegotiationUiState.Initializing -> StatusText(UiStrings.statusListening(lang))

                is NegotiationUiState.AwaitingReason -> {
                    StatusText(UiStrings.statusSpeakReason(lang))
                    Spacer(Modifier.height(24.dp))
                    PartialReason(sttState, lang)
                }

                is NegotiationUiState.AwaitingCommitment -> {
                    Text(
                        text = UiStrings.statusSpeakReason(lang),
                        color = MentorColors.TextMuted,
                        fontSize = 13.sp
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = "\"${s.phrase}\"",
                        color = MentorColors.TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 30.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(24.dp))
                    PartialReason(sttState, lang)
                }

                is NegotiationUiState.ConsultingLlm -> {
                    StatusText(UiStrings.statusMentorThinking(lang))
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "\"${s.spokenReason}\"",
                        color = MentorColors.TextMuted,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }

                is NegotiationUiState.GrantedClosing -> {
                    StatusText(UiStrings.statusGranted(lang, s.minutes))
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = s.spokenResponse,
                        color = MentorColors.TextBody,
                        fontSize = 15.sp,
                        lineHeight = 24.sp,
                        textAlign = TextAlign.Center
                    )
                }

                is NegotiationUiState.DeniedClosing -> {
                    StatusText(UiStrings.statusDenied(lang))
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = s.message,
                        color = MentorColors.TextBody,
                        fontSize = 15.sp,
                        lineHeight = 24.sp,
                        textAlign = TextAlign.Center
                    )
                }

                is NegotiationUiState.Closed -> StatusText(s.reason)
            }
        }

        // Pastki harakat tugmasi — kontextga qarab
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when (state) {
                is NegotiationUiState.AwaitingReason,
                is NegotiationUiState.AwaitingCommitment -> {
                    MicButton(
                        listening = sttState is SttState.Listening || sttState is SttState.Speaking,
                        onClick = {
                            if (sttState is SttState.Listening || sttState is SttState.Speaking) {
                                viewModel.cancelListening()
                            } else {
                                viewModel.startListening()
                            }
                        }
                    )
                }

                is NegotiationUiState.ConsultingLlm -> {
                    Text(
                        text = "kuting...",
                        color = MentorColors.TextGhost,
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                is NegotiationUiState.GrantedClosing,
                is NegotiationUiState.DeniedClosing,
                is NegotiationUiState.Closed -> {
                    MentorPrimaryButton(
                        text = UiStrings.btnClose(lang),
                        onClick = onClose
                    )
                }

                else -> Unit
            }
        }
    }
}

@Composable
private fun StatusText(text: String) {
    Text(
        text = text,
        color = MentorColors.TextPrimary,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 2.sp,
        textAlign = TextAlign.Center,
        lineHeight = 30.sp
    )
}

@Composable
private fun PartialReason(sttState: SttState, lang: uz.mentorai.focus.data.language.Language) {
    val text = when (sttState) {
        is SttState.Partial -> sttState.text
        is SttState.Final -> sttState.text
        is SttState.Error -> sttState.message
        SttState.Listening -> UiStrings.statusListening(lang)
        is SttState.Speaking -> "..."
        SttState.Processing -> "..."
        SttState.Idle -> ""
    }
    if (text.isNotBlank()) {
        Text(
            text = "\"$text\"",
            color = MentorColors.TextMuted,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}

@Composable
private fun MicButton(listening: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(if (listening) MentorColors.SignalFail else MentorColors.AccentIron),
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = if (listening) MentorColors.TextPrimary else MentorColors.SurfaceVoid
            )
        ) {
            Icon(
                Icons.Default.Mic,
                contentDescription = null,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}
