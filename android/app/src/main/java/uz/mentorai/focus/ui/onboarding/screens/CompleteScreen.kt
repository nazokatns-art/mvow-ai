package uz.mentorai.focus.ui.onboarding.screens

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.mentorai.focus.ui.components.MentorPrimaryButton
import uz.mentorai.focus.ui.onboarding.OnboardingViewModel
import uz.mentorai.focus.ui.theme.MentorColors

@Composable
fun CompleteScreen(
    viewModel: OnboardingViewModel,
    onFinish: () -> Unit
) {
    val state by viewModel.state.collectAsState()

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
                text = "TAYYOR.",
                color = MentorColors.TextPrimary,
                fontSize = 56.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp
            )
            Spacer(Modifier.height(48.dp))
            Text(
                text = "Sening so'zing:",
                color = MentorColors.TextMuted,
                fontSize = 12.sp,
                letterSpacing = 2.sp
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = "\"${state.statedGoal}\"",
                color = MentorColors.TextBody,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
            Spacer(Modifier.height(48.dp))
            Text(
                text = "${state.blockedPackages.size} ta ilova bloklash uchun belgilandi.",
                color = MentorColors.TextMuted,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Sen yaxshi lahzangda ovoz berding.\nEndi men o'sha ovozni eslataman.",
                color = MentorColors.TextMuted,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                lineHeight = 20.sp
            )
            MentorPrimaryButton(
                text = "Ishga tushirish",
                onClick = onFinish
            )
        }
    }
}
