package uz.mentorai.focus.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.mentorai.focus.ui.theme.MentorColors

/**
 * Asosiy harakat tugmasi — Dark Discipline.
 * Yumaloqlik 4dp (chiroyli emas, qattiq).
 */
@Composable
fun MentorPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MentorColors.AccentIron,
            contentColor = MentorColors.SurfaceVoid,
            disabledContainerColor = MentorColors.SurfaceIron,
            disabledContentColor = MentorColors.TextMuted
        )
    ) {
        Text(
            text = text.uppercase(),
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Ikkinchi darajali tugma — kontur, demoted.
 */
@Composable
fun MentorSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(1.dp, MentorColors.TextMuted),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MentorColors.TextBody
        )
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            letterSpacing = 1.sp
        )
    }
}

/**
 * Eng zaif harakat — qasddan xira ("override request" uchun).
 */
@Composable
fun MentorGhostButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(
            text = text.lowercase(),
            color = MentorColors.TextMuted,
            fontSize = 12.sp
        )
    }
}
