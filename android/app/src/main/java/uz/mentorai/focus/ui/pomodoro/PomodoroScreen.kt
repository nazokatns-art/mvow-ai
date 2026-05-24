package uz.mentorai.focus.ui.pomodoro

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import uz.mentorai.focus.ui.components.MentorPrimaryButton
import uz.mentorai.focus.ui.theme.MentorColors

enum class PomodoroMode(val durationMin: Int) {
    FOCUS(25),
    BREAK(5),
    LONG_BREAK(15),
    COMPLETE(0)
}

data class PomodoroSnapshot(
    val modeLabel: String,
    val heroLbl: String,
    val heroTask: String,
    val timerLabel: String,
    val mentorPrefix: String,
    val mentorRest: String,
    val primaryBtn: String,
    val insightLabel: String,
    val cycle: List<Int> // 0=empty, 1=done, 2=current
)

private val Gold = Color(0xFFE8C77E)
private val GoldDeep = Color(0xFF8A6F44)
private val Sky = Color(0xFF7CA8C9)
private val EmeraldBright = Color(0xFF6BAF7C)
private val Crimson = Color(0xFFB8334A)

/**
 * Pomodoro — 25/5 cycles + 15 long break, 4-pomodoro sets.
 * Mirrors docs/v2/preview/pomodoro.html.
 */
@Composable
fun PomodoroScreen(
    taskTitle: String = "Chuqur kod — API integratsiya",
    initialMode: PomodoroMode = PomodoroMode.FOCUS,
    onSessionDone: () -> Unit = {}
) {
    var mode by remember { mutableStateOf(initialMode) }
    var completed by remember { mutableIntStateOf(2) }
    var secondsLeft by remember { mutableIntStateOf(initialMode.durationMin * 60 - 12 * 60 - 30) }

    LaunchedEffect(mode) {
        secondsLeft = when (mode) {
            PomodoroMode.FOCUS -> 12 * 60 + 30
            PomodoroMode.BREAK -> 4 * 60 + 38
            PomodoroMode.LONG_BREAK -> 13 * 60 + 12
            PomodoroMode.COMPLETE -> 0
        }
        while (mode != PomodoroMode.COMPLETE) {
            delay(1000)
            secondsLeft = (secondsLeft - 1).coerceAtLeast(0)
            if (secondsLeft == 0) {
                mode = when (mode) {
                    PomodoroMode.FOCUS -> {
                        completed = (completed + 1).coerceAtMost(4)
                        if (completed >= 4) PomodoroMode.LONG_BREAK else PomodoroMode.BREAK
                    }
                    PomodoroMode.BREAK -> PomodoroMode.FOCUS
                    PomodoroMode.LONG_BREAK -> PomodoroMode.COMPLETE
                    PomodoroMode.COMPLETE -> PomodoroMode.COMPLETE
                }
                break
            }
        }
    }

    val snapshot = snapshotFor(mode, completed)
    val accent = when (mode) {
        PomodoroMode.BREAK -> EmeraldBright
        PomodoroMode.LONG_BREAK -> Sky
        PomodoroMode.COMPLETE -> Gold
        else -> Gold
    }
    val bg = when (mode) {
        PomodoroMode.BREAK -> Color(0xFF0A1A14)
        PomodoroMode.LONG_BREAK -> Color(0xFF0A1220)
        PomodoroMode.COMPLETE -> Color(0xFF1A1610)
        else -> Color(0xFF04060B)
    }
    val totalSec = when (mode) {
        PomodoroMode.FOCUS -> 25 * 60
        PomodoroMode.BREAK -> 5 * 60
        PomodoroMode.LONG_BREAK -> 15 * 60
        PomodoroMode.COMPLETE -> 1
    }
    val progress = 1f - (secondsLeft.toFloat() / totalSec).coerceIn(0f, 1f)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(horizontal = 20.dp)
            .padding(top = 28.dp, bottom = 28.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ModePill(label = snapshot.modeLabel, accent = accent)
            Spacer(Modifier.height(12.dp))
            HeroBlock(label = snapshot.heroLbl, task = if (mode == PomodoroMode.FOCUS) taskTitle else snapshot.heroTask, accent = accent)
            Spacer(Modifier.height(16.dp))
            CircularTimer(progress = progress, digits = formatTime(secondsLeft), label = snapshot.timerLabel, accent = accent)
            Spacer(Modifier.height(14.dp))
            CycleRow(states = snapshot.cycle, accent = accent)
            Spacer(Modifier.height(14.dp))
            MentorCard(prefix = snapshot.mentorPrefix, rest = snapshot.mentorRest, accent = accent)
            Spacer(Modifier.height(12.dp))
            InsightLabel(text = snapshot.insightLabel, accent = accent)

            Spacer(Modifier.weight(1f))
            MentorPrimaryButton(
                text = snapshot.primaryBtn,
                onClick = {
                    mode = when (mode) {
                        PomodoroMode.FOCUS -> { completed = (completed + 1).coerceAtMost(4); if (completed >= 4) PomodoroMode.LONG_BREAK else PomodoroMode.BREAK }
                        PomodoroMode.BREAK -> PomodoroMode.FOCUS
                        PomodoroMode.LONG_BREAK -> PomodoroMode.COMPLETE
                        PomodoroMode.COMPLETE -> { onSessionDone(); PomodoroMode.FOCUS }
                    }
                }
            )
            Spacer(Modifier.height(10.dp))
            ModeTabs(current = mode, onPick = { mode = it })
        }
    }
}

@Composable
private fun ModePill(label: String, accent: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(accent.copy(alpha = 0.08f))
            .border(1.dp, accent.copy(alpha = 0.4f), RoundedCornerShape(999.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(label, color = accent, fontSize = 10.sp, letterSpacing = 4.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun HeroBlock(label: String, task: String, accent: Color) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(label, color = accent, fontSize = 8.5.sp, letterSpacing = 4.sp)
        Spacer(Modifier.height(6.dp))
        Text(
            task,
            color = MentorColors.TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}

@Composable
private fun CircularTimer(progress: Float, digits: String, label: String, accent: Color) {
    val animated by animateFloatAsState(targetValue = progress, animationSpec = tween(500), label = "ring")
    Box(modifier = Modifier.size(220.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = 6.dp.toPx()
            val inset = stroke / 2
            drawCircle(color = accent.copy(alpha = 0.15f), radius = (size.minDimension - stroke) / 2, style = Stroke(width = stroke))
            drawArc(
                color = accent,
                startAngle = -90f,
                sweepAngle = 360f * animated,
                useCenter = false,
                style = Stroke(width = stroke),
                topLeft = Offset(inset, inset),
                size = Size(size.width - stroke, size.height - stroke)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(accent.copy(alpha = 0.25f))
                    .border(1.dp, accent.copy(alpha = 0.6f), CircleShape)
            )
            Spacer(Modifier.height(8.dp))
            Text(digits, color = MentorColors.TextPrimary, fontSize = 44.sp, fontWeight = FontWeight.Light, letterSpacing = 2.sp)
            Text(label, color = accent, fontSize = 9.sp, letterSpacing = 4.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun CycleRow(states: List<Int>, accent: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("4 POMODORO TSIKLI", color = MentorColors.TextMuted, fontSize = 9.sp, letterSpacing = 3.sp)
        Spacer(Modifier.width(6.dp))
        states.forEach { state ->
            val color = when (state) {
                1 -> EmeraldBright
                2 -> accent
                else -> MentorColors.TextGhost
            }
            Box(
                modifier = Modifier
                    .size(if (state == 2) 12.dp else 8.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

@Composable
private fun MentorCard(prefix: String, rest: String, accent: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(accent.copy(alpha = 0.05f))
            .border(1.dp, accent.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .clip(CircleShape)
                .background(accent.copy(alpha = 0.20f))
        )
        Spacer(Modifier.width(10.dp))
        Row {
            if (prefix.isNotEmpty()) {
                Text(prefix, color = accent, fontSize = 13.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.SemiBold, lineHeight = 19.sp)
            }
            Text(rest, color = MentorColors.TextBody, fontSize = 13.sp, fontStyle = FontStyle.Italic, lineHeight = 19.sp)
        }
    }
}

@Composable
private fun InsightLabel(text: String, accent: Color) {
    Text(text, color = accent, fontSize = 9.sp, letterSpacing = 3.sp, fontWeight = FontWeight.SemiBold)
}

@Composable
private fun ModeTabs(current: PomodoroMode, onPick: (PomodoroMode) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        ModeTab("FOKUS 25", current == PomodoroMode.FOCUS, Modifier.weight(1f)) { onPick(PomodoroMode.FOCUS) }
        ModeTab("DAM 5", current == PomodoroMode.BREAK, Modifier.weight(1f)) { onPick(PomodoroMode.BREAK) }
        ModeTab("UZUN 15", current == PomodoroMode.LONG_BREAK, Modifier.weight(1f)) { onPick(PomodoroMode.LONG_BREAK) }
        ModeTab("TUGADI", current == PomodoroMode.COMPLETE, Modifier.weight(1f)) { onPick(PomodoroMode.COMPLETE) }
    }
}

@Composable
private fun ModeTab(label: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(28.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(if (selected) Gold.copy(alpha = 0.08f) else Color.White.copy(alpha = 0.02f))
            .border(1.dp, if (selected) Gold else MentorColors.TextGhost, RoundedCornerShape(2.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            color = if (selected) Gold else MentorColors.TextMuted,
            fontSize = 9.sp,
            letterSpacing = 2.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private fun formatTime(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return "%02d:%02d".format(m, s)
}

private fun snapshotFor(mode: PomodoroMode, completed: Int): PomodoroSnapshot {
    val cycle = (0 until 4).map { i ->
        when {
            i < completed -> 1
            i == completed && mode == PomodoroMode.FOCUS -> 2
            i == completed && mode == PomodoroMode.BREAK -> 1
            i < 4 && mode == PomodoroMode.LONG_BREAK -> 1
            i < 4 && mode == PomodoroMode.COMPLETE -> 1
            else -> 0
        }
    }
    return when (mode) {
        PomodoroMode.FOCUS -> PomodoroSnapshot(
            modeLabel = "FOKUS · POMODORO ${completed + 1}",
            heroLbl = "${completed + 1}-CHI POMODORO · 4 TADAN",
            heroTask = "",
            timerLabel = "QOLDI · FOKUS",
            mentorPrefix = "${completed + 1}-pomodoro ",
            mentorRest = "davom etmoqda. Diqqat tarqalsa — bir lahza nafas chiqar, qaytib kel. 25 daqiqa miyangning eng sof attention oynasi.",
            primaryBtn = "Davom etaman",
            insightLabel = "5 DAQIQADA — MIYANG UCHUN",
            cycle = cycle
        )
        PomodoroMode.BREAK -> PomodoroSnapshot(
            modeLabel = "TANAFFUS · 5 DAQ",
            heroLbl = "POMODORO ORASIDA",
            heroTask = "Dam ol · ko'zga, tanaga, nafasga",
            timerLabel = "DAM · QOLDI",
            mentorPrefix = "",
            mentorRest = "Dam — bekorchilik emas, miyangning sifatli ish vaqti. Default Mode Network yondi — eng yangi g'oyalar shu lahzada keladi.",
            primaryBtn = "Keyingi pomodoroga",
            insightLabel = "5 DAQIQADA — MIYANG UCHUN",
            cycle = cycle
        )
        PomodoroMode.LONG_BREAK -> PomodoroSnapshot(
            modeLabel = "UZUN DAM · 15 DAQ",
            heroLbl = "4 POMODORO TUGADI",
            heroTask = "15 daqiqa — chuqur dam",
            timerLabel = "UZUN DAM · QOLDI",
            mentorPrefix = "4 ta pomodoro · ",
            mentorRest = "100 daqiqa fokus. Endi 15 daqiqa — yur, suv ich, sof havo nafasla. Keyingi 4 tsikl uchun resurs shu yerda yig'iladi.",
            primaryBtn = "Yangi 4 pomodoro",
            insightLabel = "15 DAQIQADA — CHUQUR RESET",
            cycle = cycle
        )
        PomodoroMode.COMPLETE -> PomodoroSnapshot(
            modeLabel = "BAJARILDI",
            heroLbl = "KOD SESSIYASI YOPILDI",
            heroTask = "2 soat · 4 pomodoro · API tayyor",
            timerLabel = "TUGADI",
            mentorPrefix = "100 daqiqa ",
            mentorRest = "tindirilgan fokus. API integratsiya yopildi — bu daraxtingda yana bir halqa. Hozir 15 daqiqa dam, keyin keyingi ish.",
            primaryBtn = "Tugatib — keyingi ishga",
            insightLabel = "JONLI SHU LAHZADASAN",
            cycle = cycle
        )
    }
}
