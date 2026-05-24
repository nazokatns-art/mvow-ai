package uz.mentorai.focus.ui.onboarding.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.mentorai.focus.ui.components.MentorPrimaryButton
import uz.mentorai.focus.ui.components.MentorScreenScaffold
import uz.mentorai.focus.ui.components.MentorSectionLabel
import uz.mentorai.focus.ui.onboarding.OnboardingViewModel
import uz.mentorai.focus.ui.theme.MentorColors

private data class AppCandidate(
    val pkg: String,
    val displayName: String,
    val tier: AppTier
)

private enum class AppTier(val label: String, val color: androidx.compose.ui.graphics.Color) {
    DOPAMINE("Yuqori xavf", MentorColors.SignalFail),
    SOCIAL("Ijtimoiy", MentorColors.AccentBrass),
    MEDIA("Media", MentorColors.TextBody)
}

private val DEFAULT_CANDIDATES = listOf(
    AppCandidate("com.zhiliaoapp.musically", "TikTok", AppTier.DOPAMINE),
    AppCandidate("com.instagram.android", "Instagram", AppTier.DOPAMINE),
    AppCandidate("com.google.android.youtube", "YouTube", AppTier.DOPAMINE),
    AppCandidate("com.snapchat.android", "Snapchat", AppTier.SOCIAL),
    AppCandidate("com.facebook.katana", "Facebook", AppTier.SOCIAL),
    AppCandidate("com.twitter.android", "X (Twitter)", AppTier.SOCIAL),
    AppCandidate("com.reddit.frontpage", "Reddit", AppTier.SOCIAL),
    AppCandidate("org.telegram.messenger", "Telegram", AppTier.SOCIAL),
    AppCandidate("com.whatsapp", "WhatsApp", AppTier.SOCIAL),
    AppCandidate("com.netflix.mediaclient", "Netflix", AppTier.MEDIA),
    AppCandidate("com.spotify.music", "Spotify", AppTier.MEDIA),
    AppCandidate("com.king.candycrushsaga", "Candy Crush", AppTier.MEDIA)
)

@Composable
fun AppSelectionScreen(
    viewModel: OnboardingViewModel,
    onContinue: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var selected by remember {
        mutableStateOf(
            if (state.blockedPackages.isEmpty()) {
                DEFAULT_CANDIDATES.filter { it.tier == AppTier.DOPAMINE }
                    .map { it.pkg }.toSet()
            } else state.blockedPackages
        )
    }

    MentorScreenScaffold(
        title = "Qaysi ilovalarni bloklash kerak?",
        subtitle = "Sessiya davomida ulardan birini ochsang — Mentor seni to'xtatadi.",
        bottomActions = {
            MentorPrimaryButton(
                text = "Davom etish (${selected.size} tanlandi)",
                onClick = {
                    viewModel.saveBlockedPackages(selected)
                    onContinue()
                },
                enabled = selected.isNotEmpty()
            )
        }
    ) {
        Column {
            MentorSectionLabel(text = "Tavsiya etilgan")
            Spacer(Modifier.height(12.dp))

            DEFAULT_CANDIDATES.forEach { candidate ->
                val isSelected = candidate.pkg in selected
                AppRow(
                    candidate = candidate,
                    isSelected = isSelected,
                    onToggle = {
                        selected = if (isSelected) selected - candidate.pkg
                                   else selected + candidate.pkg
                    }
                )
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(24.dp))
            Text(
                text = "Boshqa ilovalarni keyinroq sozlamalardan qo'shishingiz mumkin.",
                color = MentorColors.TextMuted,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun AppRow(
    candidate: AppCandidate,
    isSelected: Boolean,
    onToggle: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(MentorColors.SurfaceSteel)
            .border(
                1.dp,
                if (isSelected) MentorColors.AccentIron else MentorColors.TextGhost,
                RoundedCornerShape(4.dp)
            )
            .clickable { onToggle() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = candidate.displayName,
                color = MentorColors.TextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = candidate.tier.label,
                color = candidate.tier.color,
                fontSize = 11.sp,
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Bold
            )
        }
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MentorColors.AccentIron,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Spacer(Modifier.size(20.dp))
        }
    }
}
