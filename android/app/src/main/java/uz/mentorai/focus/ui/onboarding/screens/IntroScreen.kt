package uz.mentorai.focus.ui.onboarding.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.mentorai.focus.ui.components.MentorPrimaryButton
import uz.mentorai.focus.ui.components.MentorScreenScaffold
import uz.mentorai.focus.ui.theme.MentorColors

@Composable
fun IntroScreen(onContinue: () -> Unit) {
    MentorScreenScaffold(
        title = "Mentor nima qiladi?",
        subtitle = "Uch narsa. Boshqa narsa yo'q.",
        bottomActions = {
            MentorPrimaryButton(text = "Tushundim", onClick = onContinue)
        }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            IntroPoint(
                number = "01",
                title = "Bloklaydi",
                description = "Sen tanlagan ilovalar — sen tanlagan vaqtda bloklanadi. " +
                        "Mentor'ni o'chira olmaysan: sozlamalar ham bloklangan."
            )
            IntroPoint(
                number = "02",
                title = "Konfrontatsiya qiladi",
                description = "Bloklangan ilovani ochsang, AI mentor seni o'z so'zlaring bilan " +
                        "yuzlashtiradi. Yumshoq emas, lekin haqiqiy."
            )
            IntroPoint(
                number = "03",
                title = "Eslab turadi",
                description = "Sen so'z bergan paytdagi ovozingni o'zingga eshittiradi. " +
                        "O'zingdan boshqa hech kim bunday majburlay olmaydi."
            )
        }
    }
}

@Composable
private fun IntroPoint(
    number: String,
    title: String,
    description: String
) {
    Row {
        Text(
            text = number,
            color = MentorColors.AccentBrass,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
        Spacer(Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                color = MentorColors.TextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = description,
                color = MentorColors.TextBody,
                fontSize = 14.sp,
                lineHeight = 22.sp
            )
        }
    }
}
