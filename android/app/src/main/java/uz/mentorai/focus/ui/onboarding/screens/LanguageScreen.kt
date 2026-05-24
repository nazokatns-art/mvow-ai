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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.hilt.navigation.compose.hiltViewModel
import uz.mentorai.focus.data.language.Language
import uz.mentorai.focus.i18n.UiStrings
import uz.mentorai.focus.ui.components.MentorPrimaryButton
import uz.mentorai.focus.ui.components.MentorScreenScaffold
import uz.mentorai.focus.ui.onboarding.OnboardingViewModel
import uz.mentorai.focus.ui.theme.MentorColors

@Composable
fun LanguageScreen(
    viewModel: OnboardingViewModel = hiltViewModel(),
    onContinue: () -> Unit
) {
    var selected by remember { mutableStateOf(Language.systemDefault()) }

    MentorScreenScaffold(
        title = UiStrings.chooseLanguage(selected),
        subtitle = UiStrings.chooseLanguageSubtitle(selected),
        bottomActions = {
            MentorPrimaryButton(
                text = UiStrings.btnContinue(selected),
                onClick = {
                    viewModel.saveLanguage(selected)
                    onContinue()
                }
            )
        }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Language.entries.forEach { language ->
                LanguageOption(
                    language = language,
                    isSelected = language == selected,
                    onClick = { selected = language }
                )
            }
        }
    }
}

@Composable
private fun LanguageOption(
    language: Language,
    isSelected: Boolean,
    onClick: () -> Unit
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
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Column(Modifier.fillMaxWidth().padding(end = 24.dp)) {
            Text(
                text = language.displayName,
                color = MentorColors.TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = language.englishName,
                color = MentorColors.TextMuted,
                fontSize = 12.sp
            )
        }
        if (isSelected) {
            Row(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(MentorColors.AccentIron),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.Check,
                    contentDescription = null,
                    tint = MentorColors.SurfaceVoid,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
