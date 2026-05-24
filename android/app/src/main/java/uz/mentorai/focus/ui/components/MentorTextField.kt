package uz.mentorai.focus.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.mentorai.focus.ui.theme.MentorColors

@Composable
fun MentorTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    helperText: String? = null,
    isError: Boolean = false
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = MentorColors.TextGhost,
                    fontSize = 16.sp
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = if (minLines > 1) 120.dp else 56.dp),
            shape = RoundedCornerShape(4.dp),
            minLines = minLines,
            maxLines = maxLines,
            isError = isError,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MentorColors.TextPrimary,
                unfocusedTextColor = MentorColors.TextPrimary,
                focusedBorderColor = MentorColors.AccentIron,
                unfocusedBorderColor = MentorColors.TextMuted,
                errorBorderColor = MentorColors.SignalFail,
                cursorColor = MentorColors.AccentIron,
                focusedContainerColor = MentorColors.SurfaceSteel,
                unfocusedContainerColor = MentorColors.SurfaceSteel
            )
        )
        if (helperText != null) {
            Text(
                text = helperText,
                color = if (isError) MentorColors.SignalFail else MentorColors.TextMuted,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }
    }
}

@Composable
fun MentorSectionLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text.uppercase(),
        color = MentorColors.TextMuted,
        fontSize = 11.sp,
        fontWeight = FontWeight.Medium,
        letterSpacing = 2.sp,
        modifier = modifier
    )
}
