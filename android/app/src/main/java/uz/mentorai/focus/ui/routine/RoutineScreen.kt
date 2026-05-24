package uz.mentorai.focus.ui.routine

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import uz.mentorai.focus.ui.components.MentorPrimaryButton
import uz.mentorai.focus.ui.theme.MentorColors

enum class RoutineStep { Q1_GOAL, Q2_ENERGY, Q3_TIME, Q4_HABITS, GENERATING, PLAN }

enum class RoutineScope { TODAY, WEEK }

data class Answer(val key: String, val icon: String, val name: String)

data class PlanTask(
    val time: String,
    val name: String,
    val meta: String,
    val durationMin: Int,
    val kind: PlanTaskKind = PlanTaskKind.FOCUS,
    val recurring: Boolean = false
)

enum class PlanTaskKind { FOCUS, INTENSE, REST }

private val Gold = Color(0xFFE8C77E)
private val GoldDeep = Color(0xFF8A6F44)
private val GoldFlash = Color(0xFFFFE9B5)
private val Emerald = Color(0xFF4A8A5C)
private val EmeraldBright = Color(0xFF6BAF7C)
private val Sky = Color(0xFF7CA8C9)
private val Crimson = Color(0xFFB8334A)

@Composable
fun RoutineScreen(
    onBack: () -> Unit = {},
    onPlanReady: (energy: String, time: String, goals: List<String>, habits: List<String>, scope: RoutineScope) -> Unit = { _, _, _, _, _ -> }
) {
    var step by remember { mutableStateOf(RoutineStep.Q1_GOAL) }
    val goals = remember { mutableStateListOf<String>() }
    var energy by remember { mutableStateOf("") }
    var timeBudget by remember { mutableStateOf("") }
    val habits = remember { mutableStateListOf<String>() }
    var scope by remember { mutableStateOf(RoutineScope.TODAY) }

    LaunchedEffect(step) {
        if (step == RoutineStep.GENERATING) {
            delay(1800)
            step = RoutineStep.PLAN
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MentorColors.SurfaceVoid)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 32.dp, bottom = 120.dp)
        ) {
            Header(step = step, onBack = onBack)
            Spacer(Modifier.height(18.dp))

            when (step) {
                RoutineStep.Q1_GOAL -> Question(
                    text = "Bugun ",
                    emText = "nima",
                    rest = " qilmoqchisan?",
                    prompt = "— 2-3 ta tanla —",
                    answers = q1Answers,
                    selectedKeys = goals,
                    multi = true,
                    onPick = { key ->
                        if (goals.contains(key)) goals.remove(key)
                        else if (goals.size < 3) goals.add(key)
                    }
                )

                RoutineStep.Q2_ENERGY -> Question(
                    text = "Hozir ",
                    emText = "qancha kuchli",
                    rest = "san?",
                    prompt = "",
                    answers = q2Answers,
                    selectedKeys = if (energy.isEmpty()) emptyList() else listOf(energy),
                    multi = false,
                    onPick = { energy = it }
                )

                RoutineStep.Q3_TIME -> Question(
                    text = "",
                    emText = "Qancha",
                    rest = " vaqting bor?",
                    prompt = "",
                    answers = q3Answers,
                    selectedKeys = if (timeBudget.isEmpty()) emptyList() else listOf(timeBudget),
                    multi = false,
                    onPick = { timeBudget = it }
                )

                RoutineStep.Q4_HABITS -> HabitsBlock(
                    selectedKeys = habits,
                    onToggle = { key ->
                        if (habits.contains(key)) habits.remove(key) else habits.add(key)
                    },
                    scope = scope,
                    onScopeChange = { scope = it }
                )

                RoutineStep.GENERATING -> GeneratingBlock()

                RoutineStep.PLAN -> PlanBlock(energy = energy, habits = habits, scope = scope)
            }
        }

        BottomBar(
            step = step,
            canAdvance = canAdvance(step, goals, energy, timeBudget),
            onAdvance = {
                step = when (step) {
                    RoutineStep.Q1_GOAL -> RoutineStep.Q2_ENERGY
                    RoutineStep.Q2_ENERGY -> RoutineStep.Q3_TIME
                    RoutineStep.Q3_TIME -> RoutineStep.Q4_HABITS
                    RoutineStep.Q4_HABITS -> RoutineStep.GENERATING
                    RoutineStep.GENERATING -> RoutineStep.PLAN
                    RoutineStep.PLAN -> {
                        onPlanReady(energy, timeBudget, goals.toList(), habits.toList(), scope)
                        RoutineStep.PLAN
                    }
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

private fun canAdvance(step: RoutineStep, goals: List<String>, energy: String, time: String): Boolean = when (step) {
    RoutineStep.Q1_GOAL -> goals.isNotEmpty()
    RoutineStep.Q2_ENERGY -> energy.isNotEmpty()
    RoutineStep.Q3_TIME -> time.isNotEmpty()
    RoutineStep.Q4_HABITS -> true
    RoutineStep.GENERATING -> false
    RoutineStep.PLAN -> true
}

@Composable
private fun Header(step: RoutineStep, onBack: () -> Unit) {
    val (label, dots) = when (step) {
        RoutineStep.Q1_GOAL -> "1 / 4 · NIMA" to listOf(true, false, false, false)
        RoutineStep.Q2_ENERGY -> "2 / 4 · ENERGIYA" to listOf(true, true, false, false)
        RoutineStep.Q3_TIME -> "3 / 4 · VAQT" to listOf(true, true, true, false)
        RoutineStep.Q4_HABITS -> "4 / 4 · ODATLAR" to listOf(true, true, true, true)
        RoutineStep.GENERATING -> "QURMOQDA" to listOf(true, true, true, true)
        RoutineStep.PLAN -> "TAYYOR" to listOf(true, true, true, true)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "← BEKOR",
            color = MentorColors.TextMuted,
            fontSize = 10.sp,
            letterSpacing = 2.sp,
            modifier = Modifier.clickable { onBack() }
        )
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(Gold.copy(alpha = 0.06f))
                .border(1.dp, Gold.copy(alpha = 0.4f), RoundedCornerShape(999.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, color = Gold, fontSize = 9.sp, letterSpacing = 3.sp)
            Spacer(Modifier.width(8.dp))
            dots.forEach { active ->
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(if (active) Gold else MentorColors.TextGhost)
                )
                Spacer(Modifier.width(3.dp))
            }
        }
    }
}

@Composable
private fun Question(
    text: String,
    emText: String,
    rest: String,
    prompt: String,
    answers: List<Answer>,
    selectedKeys: List<String>,
    multi: Boolean,
    onPick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Gold.copy(alpha = 0.05f))
            .border(1.dp, Gold.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
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
            if (text.isNotEmpty()) Text(text, color = MentorColors.TextPrimary, fontSize = 18.sp, fontStyle = FontStyle.Italic, lineHeight = 26.sp)
            Text(emText, color = Gold, fontSize = 18.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.SemiBold, lineHeight = 26.sp)
            Text(rest, color = MentorColors.TextPrimary, fontSize = 18.sp, fontStyle = FontStyle.Italic, lineHeight = 26.sp)
        }
    }
    if (prompt.isNotEmpty()) {
        Spacer(Modifier.height(8.dp))
        Text(prompt, color = MentorColors.TextMuted, fontSize = 11.sp, fontStyle = FontStyle.Italic)
    }
    Spacer(Modifier.height(14.dp))
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        answers.forEach { a ->
            AnswerChip(
                icon = a.icon,
                name = a.name,
                selected = selectedKeys.contains(a.key),
                onClick = { onPick(a.key) }
            )
        }
    }
}

@Composable
private fun AnswerChip(icon: String, name: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) Gold.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.03f))
            .border(1.dp, if (selected) Gold else MentorColors.TextGhost, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, color = if (selected) Gold else MentorColors.TextBody, fontSize = 18.sp)
        Spacer(Modifier.width(14.dp))
        Text(
            name,
            color = if (selected) MentorColors.TextPrimary else MentorColors.TextBody,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun HabitsBlock(
    selectedKeys: List<String>,
    onToggle: (String) -> Unit,
    scope: RoutineScope,
    onScopeChange: (RoutineScope) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Gold.copy(alpha = 0.05f))
            .border(1.dp, Gold.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(modifier = Modifier.size(28.dp).clip(androidx.compose.foundation.shape.CircleShape).background(Gold.copy(alpha = 0.20f)))
        Spacer(Modifier.width(12.dp))
        Row {
            Text("Doimiy ", color = MentorColors.TextPrimary, fontSize = 18.sp, fontStyle = FontStyle.Italic, lineHeight = 26.sp)
            Text("odatlaring", color = Gold, fontSize = 18.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.SemiBold, lineHeight = 26.sp)
            Text(" bormi?", color = MentorColors.TextPrimary, fontSize = 18.sp, fontStyle = FontStyle.Italic, lineHeight = 26.sp)
        }
    }
    Spacer(Modifier.height(8.dp))
    Text("— har kuni qaytariladi · bo'sh qoldirsa yo'q —", color = MentorColors.TextMuted, fontSize = 11.sp, fontStyle = FontStyle.Italic)
    Spacer(Modifier.height(14.dp))
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        habitOptions.forEach { h ->
            HabitChip(
                icon = h.icon,
                name = h.name,
                selected = selectedKeys.contains(h.key),
                onClick = { onToggle(h.key) }
            )
        }
    }
    Spacer(Modifier.height(18.dp))
    Text("REJA QAMRAGI", color = GoldDeep, fontSize = 9.sp, letterSpacing = 4.sp, fontWeight = FontWeight.SemiBold)
    Spacer(Modifier.height(8.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        ScopeChip("☀ Faqat bugun", scope == RoutineScope.TODAY, Modifier.weight(1f)) { onScopeChange(RoutineScope.TODAY) }
        ScopeChip("↻ 1 hafta", scope == RoutineScope.WEEK, Modifier.weight(1f)) { onScopeChange(RoutineScope.WEEK) }
    }
}

@Composable
private fun HabitChip(icon: String, name: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) Gold.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.03f))
            .border(1.dp, if (selected) Gold else MentorColors.TextGhost, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icon, color = if (selected) Gold else MentorColors.TextBody, fontSize = 18.sp)
        Spacer(Modifier.width(14.dp))
        Text(
            name,
            color = if (selected) MentorColors.TextPrimary else MentorColors.TextBody,
            fontSize = 14.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.weight(1f)
        )
        if (selected) {
            Text("↻", color = Gold, fontSize = 16.sp)
        }
    }
}

@Composable
private fun ScopeChip(text: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) Gold.copy(alpha = 0.18f) else Color.White.copy(alpha = 0.03f))
            .border(1.dp, if (selected) Gold else MentorColors.TextGhost, RoundedCornerShape(8.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            color = if (selected) Gold else MentorColors.TextBody,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
            letterSpacing = 1.sp
        )
    }
}

@Composable
private fun GeneratingBlock() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        val transition = rememberInfiniteTransition(label = "gen")
        val rotation by transition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(tween(8000, easing = LinearEasing), RepeatMode.Restart),
            label = "spin"
        )
        Box(
            modifier = Modifier
                .size(90.dp)
                .rotate(rotation)
                .clip(CircleShape)
                .background(Sky.copy(alpha = 0.20f))
                .border(2.dp, Sky, CircleShape)
        )
        Row {
            Text("Reja tug'ilmoqda", color = Sky, fontSize = 17.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.SemiBold)
            Text("...", color = MentorColors.TextPrimary, fontSize = 17.sp, fontStyle = FontStyle.Italic)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(3) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Sky))
            }
        }
    }
}

@Composable
private fun PlanBlock(energy: String, habits: List<String>, scope: RoutineScope) {
    val recurringTasks = habits.mapNotNull { key -> habitOptions.firstOrNull { it.key == key } }.map { h ->
        PlanTask(
            time = h.defaultTime,
            name = h.name,
            meta = "${h.icon} ODAT · HAR KUN",
            durationMin = h.durationMin,
            kind = PlanTaskKind.FOCUS,
            recurring = true
        )
    }
    val allTasks = (recurringTasks + sampleGeneratedPlan).sortedBy { it.time }
    val taskCount = allTasks.size
    val totalMin = allTasks.sumOf { it.durationMin }
    val totalLabel = "${totalMin / 60}S ${totalMin % 60}D"
    val scopeLabel = if (scope == RoutineScope.WEEK) "1 HAFTA" else "BUGUN"
    val mainEm = if (scope == RoutineScope.WEEK) "haftang" else "bugun"

    Column {
        Row {
            Text("REJA TAYYOR", color = GoldFlash, fontSize = 9.sp, letterSpacing = 4.sp, fontWeight = FontWeight.SemiBold)
            Text(" · $taskCount ISH · $totalLabel · $scopeLabel", color = EmeraldBright, fontSize = 9.sp, letterSpacing = 4.sp)
        }
        Spacer(Modifier.height(6.dp))
        Row {
            Text("Mana sening ", color = MentorColors.TextPrimary, fontSize = 20.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.SemiBold)
            Text(mainEm, color = EmeraldBright, fontSize = 20.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.SemiBold)
            Text(".", color = MentorColors.TextPrimary, fontSize = 20.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.height(14.dp))
        allTasks.forEach { t ->
            PlanTaskRow(t)
            Spacer(Modifier.height(6.dp))
        }
        Spacer(Modifier.height(14.dp))
        SummaryCard()
        Spacer(Modifier.height(14.dp))
        MentorPlanNote(energy, scope, recurringTasks.size)
    }
}

@Composable
private fun PlanTaskRow(t: PlanTask) {
    val border = when {
        t.recurring -> Gold.copy(alpha = 0.5f)
        t.kind == PlanTaskKind.INTENSE -> Crimson.copy(alpha = 0.3f)
        t.kind == PlanTaskKind.REST -> Sky.copy(alpha = 0.3f)
        else -> Emerald.copy(alpha = 0.3f)
    }
    val bg = when {
        t.recurring -> Gold.copy(alpha = 0.08f)
        t.kind == PlanTaskKind.INTENSE -> Crimson.copy(alpha = 0.05f)
        t.kind == PlanTaskKind.REST -> Sky.copy(alpha = 0.05f)
        else -> Emerald.copy(alpha = 0.05f)
    }
    val timeColor = when {
        t.recurring -> Gold
        t.kind == PlanTaskKind.INTENSE -> Crimson
        t.kind == PlanTaskKind.REST -> Sky
        else -> Gold
    }
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
            Text(t.time, color = timeColor, fontSize = 11.sp, letterSpacing = 1.sp, fontWeight = FontWeight.SemiBold)
            Text("${t.durationMin} daq", color = GoldDeep, fontSize = 7.5.sp)
        }
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(t.name, color = MentorColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
                if (t.recurring) {
                    Text("↻", color = Gold, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(2.dp))
            Text(t.meta, color = GoldDeep, fontSize = 7.5.sp, letterSpacing = 1.sp)
            Spacer(Modifier.height(6.dp))
            DurationBar(durationMin = t.durationMin, accent = timeColor)
        }
    }
}

@Composable
private fun DurationBar(durationMin: Int, accent: Color) {
    val fraction = (durationMin / 120f).coerceIn(0.1f, 1f)
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(accent.copy(alpha = 0.18f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(accent)
            )
        }
        Spacer(Modifier.width(8.dp))
        val label = if (durationMin >= 60) "${durationMin / 60}S" else "${durationMin}D"
        Text(label, color = accent, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun SummaryCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Gold.copy(alpha = 0.05f))
            .border(1.dp, Gold.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(14.dp)
    ) {
        SummaryRow("Fokus", "3s 45d")
        SummaryRow("Tana", "45d")
        SummaryRow("Dam", "15d")
        SummaryRow("Yaqinlar", "45d")
    }
}

@Composable
private fun SummaryRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = MentorColors.TextBody, fontSize = 13.sp, fontStyle = FontStyle.Italic)
        Text(value, color = Gold, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun MentorPlanNote(energy: String, scope: RoutineScope, habitCount: Int) {
    val energyLabel = when (energy) {
        "high" -> "kuchli"
        "medium" -> "o'rta"
        "tired" -> "tushgan"
        else -> "o'rta"
    }
    val extra = buildString {
        if (habitCount > 0) append(" $habitCount ta doimiy odat — har kuni qaytaradi.")
        if (scope == RoutineScope.WEEK) append(" Bu reja 7 kunga avtomatik chiziladi.")
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(Emerald.copy(alpha = 0.06f))
            .border(1.dp, Emerald.copy(alpha = 0.4f), RoundedCornerShape(4.dp))
            .padding(14.dp)
    ) {
        Row {
            Text(
                "Energiyang ",
                color = MentorColors.TextBody,
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 20.sp
            )
            Text(
                energyLabel,
                color = EmeraldBright,
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp
            )
            Text(
                " — chuqur ishni tongga qo'ydim. 11:00 — dam, 14:00 — yana fokus.$extra",
                color = MentorColors.TextBody,
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun BottomBar(
    step: RoutineStep,
    canAdvance: Boolean,
    onAdvance: () -> Unit,
    modifier: Modifier = Modifier
) {
    val label = when (step) {
        RoutineStep.Q1_GOAL, RoutineStep.Q2_ENERGY, RoutineStep.Q3_TIME -> "Keyingi"
        RoutineStep.Q4_HABITS -> "Reja tuz"
        RoutineStep.GENERATING -> ""
        RoutineStep.PLAN -> "Kunni boshlayman"
    }
    if (label.isEmpty()) return

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MentorColors.SurfaceVoid)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        MentorPrimaryButton(text = label, onClick = onAdvance, enabled = canAdvance)
    }
}

private val q1Answers = listOf(
    Answer("work", "◆", "Ish · loyiha"),
    Answer("study", "▦", "O'qish · imtihon"),
    Answer("sport", "⊙", "Sport · tana"),
    Answer("family", "∞", "Yaqinlar")
)

private val q2Answers = listOf(
    Answer("high", "⚡", "Kuchli"),
    Answer("medium", "🌥", "O'rta"),
    Answer("tired", "🌙", "Charchadim")
)

private val q3Answers = listOf(
    Answer("short", "⊙", "3-4 soat"),
    Answer("normal", "∼", "6-8 soat"),
    Answer("open", "∞", "10+ soat")
)

private data class HabitOption(
    val key: String,
    val icon: String,
    val name: String,
    val defaultTime: String,
    val durationMin: Int
)

private val habitOptions = listOf(
    HabitOption("morning_prayer", "☾", "Tonggi namoz", "05:30", 15),
    HabitOption("morning_light", "☀", "Tonggi quyosh · 5 daq", "06:30", 5),
    HabitOption("sport", "⊙", "Sport · harakat", "07:00", 30),
    HabitOption("reading", "▥", "Mutolaa · 30 daq", "21:30", 30),
    HabitOption("sleep_anchor", "☽", "Uyqu vaqti — telefon o'chadi", "22:30", 5)
)

private val sampleGeneratedPlan = listOf(
    PlanTask("07:00", "Yugurish · 5 km", "⊙ TANANI UYG'OTISH", 45, PlanTaskKind.FOCUS),
    PlanTask("09:00", "Imtihonga tayyorlanish", "▦ POMODORO · 4 SIKL", 120, PlanTaskKind.INTENSE),
    PlanTask("11:00", "Dam · sayr", "∼ HAVODA YURISH", 15, PlanTaskKind.REST),
    PlanTask("14:00", "Loyiha · kod yozish", "◆ CHUQUR ISH", 60, PlanTaskKind.FOCUS),
    PlanTask("19:00", "Oilaga vaqt · birga ovqat", "∞ TELEFONSIZ", 45, PlanTaskKind.FOCUS)
)
