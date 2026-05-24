package uz.mentorai.focus.ui.evening

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.mentorai.focus.ui.components.MentorPrimaryButton
import uz.mentorai.focus.ui.theme.MentorColors

data class DraftTask(
    val time: String,
    val durationLabel: String,
    val name: String,
    val meta: String,
    val severityMax: Boolean = false
)

private val Gold = Color(0xFFE8C77E)
private val GoldDeep = Color(0xFF8A6F44)
private val Twilight = Color(0xFF6B5C8E)
private val Crimson = Color(0xFFB8334A)
private val EveningTint = Color(0xFF06070F)

/**
 * Evening planning — user drafts tomorrow's day, mentor seals it.
 * Mirrors docs/v2/preview/tomorrow-plan.html.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TomorrowPlanScreen(
    initialTasks: List<DraftTask> = sampleDraftTasks,
    onSeal: (tasks: List<DraftTask>) -> Unit,
    onSkipToMorning: () -> Unit = {}
) {
    val tasks = remember { mutableStateListOf<DraftTask>().apply { addAll(initialTasks) } }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(EveningTint)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 32.dp, bottom = 140.dp)
        ) {
            EveningHeader()
            Spacer(Modifier.height(18.dp))
            MentorInvite()
            Spacer(Modifier.height(18.dp))
            SectionTitle("ERTANGI ISHLAR · 11-MAY", "${tasks.size}")
            Spacer(Modifier.height(10.dp))

            tasks.toList().forEachIndexed { i, t ->
                DraftRow(task = t, onDelete = { tasks.removeAt(i) })
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(18.dp))
            SectionTitle("TEZ QO'SH", null)
            Spacer(Modifier.height(8.dp))
            QuickAddChips(onAdd = { kind ->
                tasks.add(
                    DraftTask(
                        time = "—:—",
                        durationLabel = "30 daq",
                        name = kind,
                        meta = "⊙ YANGI · 30 DAQ"
                    )
                )
            })

            Spacer(Modifier.height(18.dp))
            VoicePlanningRow()

            Spacer(Modifier.height(18.dp))
            DayOverviewCard(tasks = tasks)

            Spacer(Modifier.height(18.dp))
            MentorClosing()
        }

        BottomSealBar(
            onSeal = { onSeal(tasks.toList()) },
            onSkip = onSkipToMorning,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun EveningHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Pill(text = "☾  21:14 · ERTANGI REJA", border = Twilight, tint = Twilight.copy(alpha = 0.10f), textColor = Twilight)
        Pill(text = "KECHQURUN", border = GoldDeep, tint = Gold.copy(alpha = 0.04f), textColor = GoldDeep)
    }
}

@Composable
private fun Pill(text: String, border: Color, tint: Color, textColor: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(tint)
            .border(1.dp, border, RoundedCornerShape(999.dp))
            .padding(horizontal = 12.dp, vertical = 5.dp)
    ) {
        Text(text = text, color = textColor, fontSize = 9.sp, letterSpacing = 4.sp)
    }
}

@Composable
private fun MentorInvite() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(Gold.copy(alpha = 0.05f))
            .border(1.dp, Gold.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Gold.copy(alpha = 0.20f))
        )
        Spacer(Modifier.width(12.dp))
        Row {
            Text(
                "Ertangi yo'lni hozir chizamiz. Tongda ",
                color = MentorColors.TextPrimary,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 21.sp
            )
            Text(
                "tasdiqlaysan",
                color = Gold,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 21.sp
            )
            Text(
                " — kerak bo'lsa o'zgartirib qaytadan.",
                color = MentorColors.TextPrimary,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 21.sp
            )
        }
    }
}

@Composable
private fun SectionTitle(label: String, count: String?) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = GoldDeep, fontSize = 9.sp, letterSpacing = 4.sp)
        Spacer(Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(GoldDeep.copy(alpha = 0.5f))
        )
        if (count != null) {
            Spacer(Modifier.width(10.dp))
            Text(count, color = Gold, fontSize = 9.sp, letterSpacing = 1.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun DraftRow(task: DraftTask, onDelete: () -> Unit) {
    val border = if (task.severityMax) Crimson.copy(alpha = 0.40f) else GoldDeep.copy(alpha = 0.40f)
    val bg = if (task.severityMax) Crimson.copy(alpha = 0.04f) else Gold.copy(alpha = 0.04f)
    val timeColor = if (task.severityMax) Crimson else Gold

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.width(54.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(task.time, color = timeColor, fontSize = 11.sp, letterSpacing = 1.sp, fontWeight = FontWeight.SemiBold)
            Text(task.durationLabel, color = GoldDeep, fontSize = 8.sp)
        }
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(task.name, color = MentorColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(2.dp))
            Text(task.meta, color = GoldDeep, fontSize = 8.sp, letterSpacing = 1.sp)
        }
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.04f))
                .clickable { onDelete() },
            contentAlignment = Alignment.Center
        ) {
            Text("×", color = MentorColors.TextMuted, fontSize = 16.sp)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun QuickAddChips(onAdd: (String) -> Unit) {
    val kinds = listOf(
        "⊙ Sport", "◇ Kod", "▥ Kitob", "⌖ Til o'rganish",
        "∼ Pomodoro", "☼ Oila", "+ Boshqa"
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        kinds.forEach { label ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color.White.copy(alpha = 0.03f))
                    .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(999.dp))
                    .clickable { onAdd(label.substringAfter(" ")) }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(label, color = MentorColors.TextBody, fontSize = 11.sp, letterSpacing = 1.sp)
            }
        }
    }
}

@Composable
private fun VoicePlanningRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.02f))
            .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Gold.copy(alpha = 0.15f))
                .border(1.dp, Gold, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("●", color = Gold, fontSize = 14.sp)
        }
        Spacer(Modifier.width(12.dp))
        Row(modifier = Modifier.weight(1f)) {
            Text(
                "Yoki ",
                color = MentorColors.TextMuted,
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic
            )
            Text(
                "ovoz bilan ayt",
                color = Gold,
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                " — \"ertaga ertalab yugurish...\"",
                color = MentorColors.TextMuted,
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun DayOverviewCard(tasks: List<DraftTask>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Twilight.copy(alpha = 0.08f))
            .border(1.dp, Twilight.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(14.dp)
    ) {
        Text("ERTANGI MANZARA", color = Twilight, fontSize = 8.5.sp, letterSpacing = 4.sp)
        Spacer(Modifier.height(8.dp))
        OverviewLine("Jami fokus", "4 soat 45 daq")
        OverviewLine("Sessiya soni", "${tasks.size} ta")
        OverviewLine("Birinchi uyg'onish", "06:15")
        OverviewLine("Erkin vaqt", "3 soat 30 daq")
    }
}

@Composable
private fun OverviewLine(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = MentorColors.TextBody, fontSize = 13.sp, fontStyle = FontStyle.Italic)
        Text(value, color = MentorColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun MentorClosing() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Gold.copy(alpha = 0.05f), RoundedCornerShape(4.dp))
            .border(1.dp, Gold.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
            .padding(14.dp)
    ) {
        Row {
            Text(
                "Yaxshi reja. ",
                color = Gold,
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp
            )
            Text(
                "Erta uyg'onish bilan kun kuchli boshlanadi. Tushga til darsi — miya hali toza paytda. Endi yot, men ertaga 06:15 da chaqiraman.",
                color = MentorColors.TextBody,
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun BottomSealBar(
    onSeal: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(EveningTint)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MentorPrimaryButton(
            text = "✓  Rejani muhrlayman",
            onClick = onSeal
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "— ertalab tuzataman —",
            color = MentorColors.TextMuted,
            fontSize = 12.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.clickable { onSkip() }
        )
    }
}

private val sampleDraftTasks = listOf(
    DraftTask("06:30", "15 daq", "Tonggi yengillik", "⊙ STRETCH + QUYOSH · 15 DAQ"),
    DraftTask("07:00", "45 daq", "Yugurish · 5 km", "⊙ SPORT · 45 DAQ"),
    DraftTask("09:00", "2 soat", "Chuqur kod — API", "⊙ POMODORO · 4 SIKL · 2S", severityMax = true),
    DraftTask("13:00", "45 daq", "Ingliz tili · B2 darsi", "↗ ZOOM · MEETING · 45 DAQ"),
    DraftTask("19:00", "60 daq", "Kitob o'qish · Atomic Habits", "⊙ FOKUS · 3 BOB · 60 DAQ")
)
