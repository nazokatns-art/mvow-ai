package uz.mentorai.focus.ui.morning

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.mentorai.focus.ui.components.MentorPrimaryButton
import uz.mentorai.focus.ui.theme.MentorColors

/** Action a user picked for a planned task in the morning review. */
enum class TaskAction { KEEP, MOVE, CANCEL }

/** Energy level user reports right before starting the day. */
enum class Energy { STRONG, MEDIUM, TIRED }

data class PlannedTask(
    val time: String,
    val durationLabel: String,
    val name: String,
    val meta: String,
    val severityMax: Boolean = false,
    val initial: TaskAction = TaskAction.KEEP
)

private val Gold = Color(0xFFE8C77E)
private val GoldDeep = Color(0xFF8A6F44)
private val GoldDim = Color(0xFFC7A36B)
private val Crimson = Color(0xFFB8334A)
private val EmeraldBright = Color(0xFF6BAF7C)
private val MorningTint = Color(0xFF1A1610)

/**
 * Morning ritual — user reviews last night's plan and confirms (or amends).
 * Mirrors docs/v2/preview/morning-confirm.html.
 */
@Composable
fun MorningConfirmScreen(
    tasks: List<PlannedTask> = sampleTasks,
    onConfirm: (changedCount: Int, pickedTaskIndex: Int, energy: Energy) -> Unit,
    onReopenPlan: () -> Unit = {}
) {
    val actions = remember {
        mutableStateListOf<TaskAction>().apply { addAll(tasks.map { it.initial }) }
    }
    var pickedTask by remember { mutableStateOf(2) }
    var energy by remember { mutableStateOf(Energy.MEDIUM) }

    val changes = actions.indices.count { actions[it] != tasks[it].initial }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MorningTint)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 32.dp, bottom = 140.dp)
        ) {
            HeaderRow()
            Spacer(Modifier.height(18.dp))
            Hero(taskCount = tasks.size)
            Spacer(Modifier.height(18.dp))
            PivotalQuote()
            Spacer(Modifier.height(16.dp))
            SectionTitle(label = "REJA · HAR BIRIGA BAHO", stat = "${tasks.size} ISH")
            Spacer(Modifier.height(10.dp))

            tasks.forEachIndexed { i, t ->
                TaskRow(
                    task = t,
                    action = actions[i],
                    onAction = { actions[i] = it }
                )
                Spacer(Modifier.height(8.dp))
            }

            Spacer(Modifier.height(20.dp))
            TopPriorityCard(
                tasks = tasks,
                picked = pickedTask,
                onPick = { pickedTask = it }
            )
            Spacer(Modifier.height(16.dp))
            EnergyCard(energy = energy, onPick = { energy = it })
        }

        BottomConfirmBar(
            changes = changes,
            onConfirm = { onConfirm(changes, pickedTask, energy) },
            onReopen = onReopenPlan,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun HeaderRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Pill(text = "☀  06:32 · TONG", border = Gold, tint = Gold.copy(alpha = 0.10f), textColor = Gold)
        Pill(text = "TASDIQ", border = GoldDeep, tint = Gold.copy(alpha = 0.04f), textColor = GoldDeep)
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
private fun Hero(taskCount: Int) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("XAYRLI TONG", color = GoldDeep, fontSize = 8.5.sp, letterSpacing = 4.sp)
        Spacer(Modifier.height(8.dp))
        Row {
            Text(
                "Kechagi rejang ",
                color = MentorColors.TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Italic
            )
            Text(
                "esingdami",
                color = Gold,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Italic
            )
            Text(
                "?",
                color = MentorColors.TextPrimary,
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                fontStyle = FontStyle.Italic
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            "11 MAY · SESHANBA · $taskCount ISH CHIZILGAN",
            color = GoldDeep,
            fontSize = 9.sp,
            letterSpacing = 2.sp
        )
    }
}

@Composable
private fun PivotalQuote() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Gold.copy(alpha = 0.06f), RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
            .border(
                width = 2.dp,
                color = Gold,
                shape = RoundedCornerShape(0.dp)
            )
            .padding(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .width(26.dp)
                .height(26.dp)
                .clip(RoundedCornerShape(13.dp))
                .background(Gold.copy(alpha = 0.20f))
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = "Kecha 21:14 da bu rejani muhrlading. Endi tongda — yangi nazar bilan ko'r. " +
                "Hammasi hali ham to'g'rimi? Birortasi shu kunga to'g'ri kelmasa, bemalol surib qo'y.",
            color = MentorColors.TextPrimary,
            fontSize = 15.sp,
            fontStyle = FontStyle.Italic,
            fontWeight = FontWeight.Medium,
            lineHeight = 22.sp
        )
    }
}

@Composable
private fun SectionTitle(label: String, stat: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, color = GoldDeep, fontSize = 9.sp, letterSpacing = 4.sp)
        Spacer(Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(GoldDeep.copy(alpha = 0.5f))
        )
        Spacer(Modifier.width(10.dp))
        Text(stat, color = Gold, fontSize = 8.5.sp, letterSpacing = 1.sp)
    }
}

@Composable
private fun TaskRow(task: PlannedTask, action: TaskAction, onAction: (TaskAction) -> Unit) {
    val borderColor = when {
        action == TaskAction.KEEP && task.severityMax -> Crimson.copy(alpha = 0.6f)
        action == TaskAction.KEEP -> EmeraldBright
        action == TaskAction.MOVE -> GoldDeep
        else -> MentorColors.TextGhost
    }
    val bg = when (action) {
        TaskAction.KEEP -> EmeraldBright.copy(alpha = 0.08f)
        TaskAction.MOVE -> Gold.copy(alpha = 0.04f)
        TaskAction.CANCEL -> Color.Transparent
    }
    val timeColor = when {
        task.severityMax && action == TaskAction.KEEP -> Crimson
        action == TaskAction.KEEP -> EmeraldBright
        else -> Gold
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(bg)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.width(54.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(task.time, color = timeColor, fontSize = 11.sp, letterSpacing = 1.sp, fontWeight = FontWeight.SemiBold)
            Text(task.durationLabel, color = GoldDeep, fontSize = 7.5.sp)
        }
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                task.name,
                color = MentorColors.TextPrimary,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(2.dp))
            Text(task.meta, color = GoldDeep, fontSize = 7.5.sp, letterSpacing = 1.sp)
            Spacer(Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                ActionChip("✓ QOLDIR", action == TaskAction.KEEP, EmeraldBright) { onAction(TaskAction.KEEP) }
                ActionChip("→ SUR", action == TaskAction.MOVE, GoldDim) { onAction(TaskAction.MOVE) }
                ActionChip("× O'CHIR", action == TaskAction.CANCEL, Crimson) { onAction(TaskAction.CANCEL) }
            }
        }
    }
}

@Composable
private fun ActionChip(
    text: String,
    selected: Boolean,
    selectedColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .height(28.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(if (selected) selectedColor.copy(alpha = 0.20f) else Color.White.copy(alpha = 0.03f))
            .border(
                width = 1.dp,
                color = if (selected) selectedColor else MentorColors.TextGhost,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            color = if (selected) selectedColor else MentorColors.TextMuted,
            fontSize = 9.sp,
            letterSpacing = 1.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun TopPriorityCard(tasks: List<PlannedTask>, picked: Int, onPick: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Gold.copy(alpha = 0.05f))
            .border(1.dp, GoldDeep.copy(alpha = 0.40f), RoundedCornerShape(8.dp))
            .padding(14.dp)
    ) {
        Text("BITTA SAVOL · ENG MUHIM", color = GoldDeep, fontSize = 9.sp, letterSpacing = 4.sp)
        Spacer(Modifier.height(8.dp))
        Row {
            Text(
                "Bu kun tugagach, ",
                color = MentorColors.TextPrimary,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic
            )
            Text(
                "qaysi bittasi",
                color = Gold,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                " bajarilmasa, achinasan?",
                color = MentorColors.TextPrimary,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic
            )
        }
        Spacer(Modifier.height(10.dp))
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            tasks.chunked(3).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    row.forEachIndexed { idx, t ->
                        val realIdx = tasks.indexOf(t)
                        PickChip(
                            text = t.name.substringBefore(" ·"),
                            selected = realIdx == picked,
                            onClick = { onPick(realIdx) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    repeat(3 - row.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun PickChip(text: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(32.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(if (selected) Gold.copy(alpha = 0.18f) else Color.White.copy(alpha = 0.02f))
            .border(1.dp, if (selected) Gold else MentorColors.TextGhost, RoundedCornerShape(4.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            color = if (selected) Gold else MentorColors.TextBody,
            fontSize = 10.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            maxLines = 1
        )
    }
}

@Composable
private fun EnergyCard(energy: Energy, onPick: (Energy) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.02f))
            .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(8.dp))
            .padding(14.dp)
    ) {
        Text("HOZIRGI ENERGIYANG?", color = GoldDeep, fontSize = 9.sp, letterSpacing = 4.sp)
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            EnergyChip("⚡ KUCHLI", energy == Energy.STRONG, Modifier.weight(1f)) { onPick(Energy.STRONG) }
            EnergyChip("🌥 O'RTA", energy == Energy.MEDIUM, Modifier.weight(1f)) { onPick(Energy.MEDIUM) }
            EnergyChip("🌙 CHARCHADIM", energy == Energy.TIRED, Modifier.weight(1f)) { onPick(Energy.TIRED) }
        }
    }
}

@Composable
private fun EnergyChip(text: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(if (selected) Gold.copy(alpha = 0.16f) else Color.White.copy(alpha = 0.02f))
            .border(1.dp, if (selected) Gold else MentorColors.TextGhost, RoundedCornerShape(4.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            color = if (selected) Gold else MentorColors.TextBody,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BottomConfirmBar(
    changes: Int,
    onConfirm: () -> Unit,
    onReopen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MorningTint)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MentorPrimaryButton(
            text = if (changes > 0) "O'zgartirdim ($changes) — qayta tasdiqla"
                   else "Tasdiqladim — birinchi ishga",
            onClick = onConfirm
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "— rejani qaytadan ko'raman —",
            color = MentorColors.TextMuted,
            fontSize = 11.sp,
            modifier = Modifier.clickable { onReopen() }
        )
    }
}

private val sampleTasks = listOf(
    PlannedTask("06:45", "15 daq", "Tonggi yengillik", "⊙ STRETCH + QUYOSH · 15 DAQ"),
    PlannedTask("07:00", "45 daq", "Yugurish · 5 km", "⊙ SPORT · 45 DAQ"),
    PlannedTask("09:00", "2 soat", "Chuqur kod — API", "⊙ POMODORO · 4 SIKL · 2S", severityMax = true),
    PlannedTask("13:00", "→ 15:00", "Ingliz tili · B2 darsi", "↗ ZOOM · 14:00 → 15:00 ga ko'chdi", initial = TaskAction.MOVE),
    PlannedTask("19:00", "60 daq", "Kitob o'qish · Atomic Habits", "⊙ FOKUS · 3 BOB · 60 DAQ")
)
