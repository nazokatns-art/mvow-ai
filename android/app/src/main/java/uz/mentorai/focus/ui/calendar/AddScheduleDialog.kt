package uz.mentorai.focus.ui.calendar

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.mentorai.focus.ui.components.MentorTextField
import uz.mentorai.focus.ui.theme.MentorColors
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun AddScheduleDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, goal: String, startMs: Long, durationMinutes: Int) -> Unit
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("") }
    var startMs by remember {
        mutableLongStateOf(
            Calendar.getInstance().apply {
                add(Calendar.HOUR, 1)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
            }.timeInMillis
        )
    }
    var duration by remember { mutableStateOf(45) }

    val isValid = title.isNotBlank() && goal.length >= 5 && duration in 5..240

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MentorColors.SurfaceSteel,
        titleContentColor = MentorColors.TextPrimary,
        textContentColor = MentorColors.TextBody,
        title = {
            Text(
                text = "Yangi reja",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                MentorTextField(
                    value = title,
                    onValueChange = { if (it.length <= 60) title = it },
                    placeholder = "Vazifa nomi (masalan: Qur'on darsi)"
                )
                MentorTextField(
                    value = goal,
                    onValueChange = { if (it.length <= 200) goal = it },
                    placeholder = "Maqsad — qisqacha",
                    minLines = 2,
                    maxLines = 4
                )

                // Vaqt tanlash
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    PickerChip(
                        label = formatDate(startMs),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            val cal = Calendar.getInstance().apply { timeInMillis = startMs }
                            DatePickerDialog(
                                context,
                                { _, y, m, d ->
                                    cal.set(y, m, d)
                                    startMs = cal.timeInMillis
                                },
                                cal.get(Calendar.YEAR),
                                cal.get(Calendar.MONTH),
                                cal.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        }
                    )
                    PickerChip(
                        label = formatTime(startMs),
                        modifier = Modifier.weight(1f),
                        onClick = {
                            val cal = Calendar.getInstance().apply { timeInMillis = startMs }
                            TimePickerDialog(
                                context,
                                { _, h, m ->
                                    cal.set(Calendar.HOUR_OF_DAY, h)
                                    cal.set(Calendar.MINUTE, m)
                                    cal.set(Calendar.SECOND, 0)
                                    startMs = cal.timeInMillis
                                },
                                cal.get(Calendar.HOUR_OF_DAY),
                                cal.get(Calendar.MINUTE),
                                true
                            ).show()
                        }
                    )
                }

                // Davomiyligi
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf(15, 30, 45, 60, 90, 120).forEach { mins ->
                        DurationChip(
                            label = "$mins",
                            selected = mins == duration,
                            modifier = Modifier.weight(1f),
                            onClick = { duration = mins }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(title.trim(), goal.trim(), startMs, duration) },
                enabled = isValid
            ) {
                Text(
                    text = "QO'SHISH",
                    color = if (isValid) MentorColors.AccentIron else MentorColors.TextGhost,
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "BEKOR",
                    color = MentorColors.TextMuted,
                    letterSpacing = 2.sp
                )
            }
        }
    )
}

@Composable
private fun PickerChip(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(MentorColors.SurfaceIron)
            .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(4.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp),
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = MentorColors.TextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun DurationChip(
    label: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(if (selected) MentorColors.AccentIron else MentorColors.SurfaceIron)
            .border(
                1.dp,
                if (selected) MentorColors.AccentIron else MentorColors.TextGhost,
                RoundedCornerShape(4.dp)
            )
            .clickable { onClick() },
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = label,
            color = if (selected) MentorColors.SurfaceVoid else MentorColors.TextBody,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private fun formatDate(ms: Long): String =
    SimpleDateFormat("dd MMM, EEE", Locale.getDefault()).format(Date(ms))

private fun formatTime(ms: Long): String =
    SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(ms))
