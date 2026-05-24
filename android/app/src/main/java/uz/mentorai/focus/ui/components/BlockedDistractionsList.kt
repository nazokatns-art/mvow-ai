package uz.mentorai.focus.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.mentorai.focus.ui.theme.MentorColors

/**
 * "BLOCKED DISTRACTIONS" ro'yxati — mockup'dagi strikethrough ilova nomlari.
 */
@Composable
fun BlockedDistractionsList(
    distractions: List<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "BLOCKED DISTRACTIONS",
            color = MentorColors.TextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 3.sp
        )
        Spacer(modifier = Modifier.height(4.dp))

        distractions.forEach { name ->
            DistractionRow(name = name)
        }
    }
}

@Composable
private fun DistractionRow(name: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Block,
            contentDescription = null,
            tint = MentorColors.AccentGlowDim,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = name.uppercase(),
            color = MentorColors.TextMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 1.5.sp,
            textDecoration = TextDecoration.LineThrough
        )
    }
}
