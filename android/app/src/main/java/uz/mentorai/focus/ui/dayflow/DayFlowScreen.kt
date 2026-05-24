package uz.mentorai.focus.ui.dayflow

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
import androidx.compose.ui.graphics.Brush
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

/** Day-flow state machine. */
enum class DayFlowState { READY, RUNNING, BREAK, DONE, TIRED }

enum class FlowEnergy { STRONG, MEDIUM, TIRED }

data class DayFlowSnapshot(
    val pre: String,
    val title: String,
    val goal: String,
    val timerDigits: String,
    val timerLabel: String,
    val progress: Float,
    val mentorPrefix: String,
    val mentorRest: String,
    val primaryBtn: String,
    val pomodoroVisible: Boolean,
    val pomodoroCurrent: Int = 0
)

private val Gold = Color(0xFFE8C77E)
private val GoldDim = Color(0xFFC7A36B)
private val GoldDeep = Color(0xFF8A6F44)
private val EmeraldBright = Color(0xFF6BAF7C)
private val EmeraldDeep = Color(0xFF1F3D2A)
private val Crimson = Color(0xFFB8334A)
private val Rose = Color(0xFFC28B8B)
private val Void = Color(0xFF04060B)
private val BreakTint = Color(0xFF0A1A14)
private val DoneTint = Color(0xFF1A1610)

/**
 * Day flow — central state machine for one running task.
 * Mirrors docs/v2/preview/day-flow.html.
 */
@Composable
fun DayFlowScreen(
    initialState: DayFlowState = DayFlowState.READY,
    totalTasks: Int = 7,
    currentTaskIndex: Int = 2,
    onAllTasksDone: () -> Unit = {}
) {
    var state by remember { mutableStateOf(initialState) }
    var energy by remember { mutableStateOf(FlowEnergy.MEDIUM) }
    var liveSeconds by remember { mutableIntStateOf(23 * 60 + 42) }

    LaunchedEffect(state) {
        while (state == DayFlowState.RUNNING) {
            delay(1000)
            liveSeconds = (liveSeconds - 1).coerceAtLeast(0)
            if (liveSeconds == 0) state = DayFlowState.BREAK
        }
    }

    val snapshot = remember(state, liveSeconds) { snapshotFor(state, liveSeconds) }
    val bg = when (state) {
        DayFlowState.BREAK -> BreakTint
        DayFlowState.DONE -> DoneTint
        DayFlowState.TIRED -> BreakTint
        else -> Void
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .padding(horizontal = 20.dp)
            .padding(top = 32.dp, bottom = 24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DayProgressRow(total = totalTasks, current = currentTaskIndex)
            Spacer(Modifier.height(20.dp))
            TaskBlock(snapshot)
            Spacer(Modifier.height(20.dp))
            CircularTimer(
                state = state,
                progress = snapshot.progress,
                digits = snapshot.timerDigits,
                label = snapshot.timerLabel
            )
            Spacer(Modifier.height(14.dp))
            if (snapshot.pomodoroVisible) {
                PomodoroRow(current = snapshot.pomodoroCurrent)
                Spacer(Modifier.height(14.dp))
            }
            MentorMessage(prefix = snapshot.mentorPrefix, rest = snapshot.mentorRest)
            Spacer(Modifier.height(14.dp))
            EnergyRow(
                energy = energy,
                onPick = {
                    energy = it
                    if (it == FlowEnergy.TIRED) state = DayFlowState.TIRED
                }
            )

            Spacer(Modifier.weight(1f))
            MentorPrimaryButton(
                text = snapshot.primaryBtn,
                onClick = {
                    state = when (state) {
                        DayFlowState.READY -> DayFlowState.RUNNING
                        DayFlowState.RUNNING -> DayFlowState.BREAK
                        DayFlowState.BREAK -> DayFlowState.RUNNING.also { liveSeconds = 25 * 60 }
                        DayFlowState.DONE -> { onAllTasksDone(); DayFlowState.READY }
                        DayFlowState.TIRED -> DayFlowState.BREAK
                    }
                }
            )
            Spacer(Modifier.height(8.dp))
            SecondaryRow()
        }
    }
}

@Composable
private fun DayProgressRow(total: Int, current: Int) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Row {
            Text("BUGUNGI YO'L · ", color = MentorColors.TextMuted, fontSize = 10.sp, letterSpacing = 3.sp)
            Text("${current + 1}", color = Gold, fontSize = 10.sp, letterSpacing = 3.sp, fontWeight = FontWeight.Bold)
            Text(" / $total", color = MentorColors.TextMuted, fontSize = 10.sp, letterSpacing = 3.sp)
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(total) { i ->
                val color = when {
                    i < current -> EmeraldBright
                    i == current -> Gold
                    else -> MentorColors.TextGhost
                }
                Box(
                    modifier = Modifier
                        .size(if (i == current) 10.dp else 8.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
    }
}

@Composable
private fun TaskBlock(snapshot: DayFlowSnapshot) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(snapshot.pre, color = Gold, fontSize = 9.sp, letterSpacing = 4.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))
        Text(
            snapshot.title,
            color = MentorColors.TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(6.dp))
        Text(
            snapshot.goal,
            color = MentorColors.TextBody,
            fontSize = 13.sp,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )
    }
}

@Composable
private fun CircularTimer(state: DayFlowState, progress: Float, digits: String, label: String) {
    val animated by animateFloatAsState(targetValue = progress, animationSpec = tween(600), label = "ring")
    val ringColor = when (state) {
        DayFlowState.BREAK -> EmeraldBright
        DayFlowState.DONE -> Gold
        DayFlowState.TIRED -> Rose
        else -> Gold
    }
    Box(
        modifier = Modifier.size(220.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = 6.dp.toPx()
            val inset = stroke / 2
            drawCircle(
                color = ringColor.copy(alpha = 0.15f),
                radius = (size.minDimension - stroke) / 2,
                style = Stroke(width = stroke)
            )
            drawArc(
                brush = Brush.sweepGradient(listOf(ringColor.copy(alpha = 0.4f), ringColor, ringColor)),
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
                    .background(ringColor.copy(alpha = 0.25f))
                    .border(1.dp, ringColor.copy(alpha = 0.6f), CircleShape)
            )
            Spacer(Modifier.height(8.dp))
            Text(
                digits,
                color = MentorColors.TextPrimary,
                fontSize = 44.sp,
                fontWeight = FontWeight.Light,
                letterSpacing = 2.sp
            )
            Text(label, color = ringColor, fontSize = 9.sp, letterSpacing = 4.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun PomodoroRow(current: Int) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Text("POMODORO", color = MentorColors.TextMuted, fontSize = 9.sp, letterSpacing = 3.sp)
        Spacer(Modifier.width(4.dp))
        repeat(4) { i ->
            val color = when {
                i < current - 1 -> EmeraldBright
                i == current - 1 -> Gold
                else -> MentorColors.TextGhost
            }
            Box(
                modifier = Modifier
                    .size(if (i == current - 1) 10.dp else 7.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

@Composable
private fun MentorMessage(prefix: String, rest: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Gold.copy(alpha = 0.05f))
            .border(1.dp, Gold.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(Gold.copy(alpha = 0.20f))
        )
        Spacer(Modifier.width(10.dp))
        Row {
            Text(
                prefix,
                color = Gold,
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 19.sp
            )
            Text(
                rest,
                color = MentorColors.TextBody,
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 19.sp
            )
        }
    }
}

@Composable
private fun EnergyRow(energy: FlowEnergy, onPick: (FlowEnergy) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("HOZIRGI ENERGIYANG?", color = MentorColors.TextMuted, fontSize = 9.sp, letterSpacing = 4.sp)
        Spacer(Modifier.height(6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            EnergyChip("⚡ KUCHLI", energy == FlowEnergy.STRONG, Modifier.weight(1f)) { onPick(FlowEnergy.STRONG) }
            EnergyChip("🌥 O'RTA", energy == FlowEnergy.MEDIUM, Modifier.weight(1f)) { onPick(FlowEnergy.MEDIUM) }
            EnergyChip("🌙 CHARCHADIM", energy == FlowEnergy.TIRED, Modifier.weight(1f), tiredAccent = true) { onPick(FlowEnergy.TIRED) }
        }
    }
}

@Composable
private fun EnergyChip(
    text: String,
    selected: Boolean,
    modifier: Modifier,
    tiredAccent: Boolean = false,
    onClick: () -> Unit
) {
    val accent = if (tiredAccent) Rose else Gold
    Box(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(if (selected) accent.copy(alpha = 0.16f) else Color.White.copy(alpha = 0.02f))
            .border(1.dp, if (selected) accent else MentorColors.TextGhost, RoundedCornerShape(4.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = if (selected) accent else MentorColors.TextBody, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun SecondaryRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        SecondaryButton("⏰ 5 daq keyin", Modifier.weight(1f)) {}
        SecondaryButton("⏭ O'tkazib yub", Modifier.weight(1f)) {}
    }
}

@Composable
private fun SecondaryButton(text: String, modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(4.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = MentorColors.TextMuted, fontSize = 11.sp)
    }
}

private fun snapshotFor(state: DayFlowState, liveSeconds: Int): DayFlowSnapshot = when (state) {
    DayFlowState.READY -> DayFlowSnapshot(
        pre = "4-ISH · KEYINGI",
        title = "Yugurish · 5 km",
        goal = "\"Tanani uyg'otish — kunni jonli boshlash\"",
        timerDigits = "30:00",
        timerLabel = "TAYMER · KUTYAPTI",
        progress = 0f,
        mentorPrefix = "Boshlayveraymi? ",
        mentorRest = "30 daqiqalik taymer tayyor. Sen \"ha\" desang yonadi.",
        primaryBtn = "Ha, boshlayman",
        pomodoroVisible = false
    )
    DayFlowState.RUNNING -> {
        val m = liveSeconds / 60
        val s = liveSeconds % 60
        val total = 25 * 60
        DayFlowSnapshot(
            pre = "3-ISH · CHUQUR FOKUS",
            title = "Kod yozish · API",
            goal = "\"API integratsiyasini tugatish va testlarni yozish\"",
            timerDigits = "%02d:%02d".format(m, s),
            timerLabel = "QOLDI",
            progress = 1f - (liveSeconds.toFloat() / total).coerceIn(0f, 1f),
            mentorPrefix = "2-pomodoro ",
            mentorRest = "davom etmoqda. Ko'zlaringni 20 soniya uzoq narsaga qarat — diqqat tiniqlashadi.",
            primaryBtn = "Davom etaman",
            pomodoroVisible = true,
            pomodoroCurrent = 2
        )
    }
    DayFlowState.BREAK -> DayFlowSnapshot(
        pre = "POMODORO TANAFFUS · 5 DAQ",
        title = "Dam ol — ko'z ber",
        goal = "\"Stol oldidan tur, derazaga qara, 5 daqiqa nafas ol\"",
        timerDigits = "04:38",
        timerLabel = "DAM",
        progress = 0.07f,
        mentorPrefix = "",
        mentorRest = "Dam — bekorchilik emas, miyangning sifatli ish vaqti. Qaytib kelganingda fokus 30% kuchayadi.",
        primaryBtn = "Keyingi pomodoroga",
        pomodoroVisible = true,
        pomodoroCurrent = 3
    )
    DayFlowState.DONE -> DayFlowSnapshot(
        pre = "3-ISH BAJARILDI ✓",
        title = "Kod yozish · TUGADI",
        goal = "\"45 daqiqa fokus, 4 pomodoro · API integratsiya yopildi\"",
        timerDigits = "00:00",
        timerLabel = "BAJARDING",
        progress = 1f,
        mentorPrefix = "Bajarding. ",
        mentorRest = "Yana bir halqa daraxtingda. Endi 5 daqiqa dam, keyin 4-ish — yugurish.",
        primaryBtn = "4-ishga o'taman",
        pomodoroVisible = false
    )
    DayFlowState.TIRED -> DayFlowSnapshot(
        pre = "CHARCHOQ SEZILDI",
        title = "To'xtaylik — sen muhim",
        goal = "\"Charchagan miya — yomon qaror chiqaradi. Hozir to'xtash — ertaga g'olib bo'lish.\"",
        timerDigits = "15:00",
        timerLabel = "DAM TAVSIYA",
        progress = 0.5f,
        mentorPrefix = "Sen \"charchayapman\" dedding. ",
        mentorRest = "Eshityapman. Keyingi 2 ish ertaga ko'chiriladi. Hozir — chuqurroq dam ol.",
        primaryBtn = "15 daqiqa dam olaman",
        pomodoroVisible = false
    )
}
