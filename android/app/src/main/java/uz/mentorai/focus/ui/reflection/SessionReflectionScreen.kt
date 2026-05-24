package uz.mentorai.focus.ui.reflection

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

private val Gold = Color(0xFFE8C77E)
private val GoldDeep = Color(0xFF8A6F44)
private val EmeraldBright = Color(0xFF6BAF7C)

enum class ReflectionStep { DONE, Q1_KNOWLEDGE, Q2_STRENGTH, CONFIRMED }

data class ReflectAnswer(val icon: String, val label: String)

@Composable
fun SessionReflectionScreen(
    taskTitle: String = "Qur'on darsi",
    durationLabel: String = "45 DAQ FOKUS",
    timeRange: String = "14:30 — 15:15",
    blockedApps: List<String> = listOf("INSTAGRAM", "TIKTOK", "YOUTUBE"),
    extraBlockedCount: Int = 2,
    streakDays: Int = 13,
    hoursToday: Double = 4.25,
    sessionsThisWeek: Int = 39,
    onDone: (newKnowledge: String?, strengthSource: String?) -> Unit = { _, _ -> }
) {
    var step by remember { mutableStateOf(ReflectionStep.DONE) }
    var knowledge by remember { mutableStateOf<String?>(null) }
    var strength by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F0B14))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 32.dp, bottom = 150.dp)
        ) {
            Header(step = step)
            Spacer(Modifier.height(18.dp))
            BrandSeal(text = sealFor(step))
            Spacer(Modifier.height(18.dp))
            AchievementCard(
                lbl = if (step == ReflectionStep.CONFIRMED) "BAJARILDI VA MUSTAHKAMLANDI" else "BAJARILDI · MUQADDAS",
                title = taskTitle,
                durationLabel = durationLabel,
                timeRange = timeRange,
                blockedApps = blockedApps,
                extraBlockedCount = extraBlockedCount
            )

            when (step) {
                ReflectionStep.DONE -> {
                    Spacer(Modifier.height(18.dp))
                    DoneCelebration()
                }
                ReflectionStep.Q1_KNOWLEDGE -> {
                    Spacer(Modifier.height(18.dp))
                    QuestionBlock(
                        words = listOf("Nima", "yangi", "narsani", "ko'rding", "bu", "sessiyada?"),
                        emWordIndex = 1,
                        prompt = "— miyaning yangi izi nimaga qoldi —",
                        answers = q1Answers,
                        selected = knowledge,
                        onPick = { knowledge = it }
                    )
                }
                ReflectionStep.Q2_STRENGTH -> {
                    Spacer(Modifier.height(18.dp))
                    QuestionBlock(
                        words = listOf("Bu", "kuching", "qaysi qismidan", "keldi?"),
                        emWordIndex = 2,
                        prompt = "— ongli kuch qaytarib chaqirilishi mumkin —",
                        answers = q2Answers,
                        selected = strength,
                        onPick = { strength = it }
                    )
                }
                ReflectionStep.CONFIRMED -> {
                    Spacer(Modifier.height(18.dp))
                    IdentityCard()
                    Spacer(Modifier.height(14.dp))
                    StatsUpdated(
                        streakDays = streakDays,
                        hoursToday = hoursToday,
                        sessionsThisWeek = sessionsThisWeek
                    )
                    Spacer(Modifier.height(14.dp))
                    MentorClosing()
                }
            }
        }

        BottomBar(
            step = step,
            canAdvance = when (step) {
                ReflectionStep.Q1_KNOWLEDGE -> knowledge != null
                ReflectionStep.Q2_STRENGTH -> strength != null
                else -> true
            },
            onAdvance = {
                step = when (step) {
                    ReflectionStep.DONE -> ReflectionStep.Q1_KNOWLEDGE
                    ReflectionStep.Q1_KNOWLEDGE -> ReflectionStep.Q2_STRENGTH
                    ReflectionStep.Q2_STRENGTH -> ReflectionStep.CONFIRMED
                    ReflectionStep.CONFIRMED -> { onDone(knowledge, strength); ReflectionStep.CONFIRMED }
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun Header(step: ReflectionStep) {
    val (stampText, stepIndex) = when (step) {
        ReflectionStep.DONE -> "SESSIYA TUGADI" to 1
        ReflectionStep.Q1_KNOWLEDGE -> "BAHO · SAVOL 1/2" to 2
        ReflectionStep.Q2_STRENGTH -> "BAHO · SAVOL 2/2" to 3
        ReflectionStep.CONFIRMED -> "TASDIQLANDI" to 3
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Pill(text = "✓  $stampText")
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(Gold.copy(alpha = 0.06f))
                .border(1.dp, Gold.copy(alpha = 0.4f), RoundedCornerShape(999.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("$stepIndex/3", color = Gold, fontSize = 10.sp, letterSpacing = 3.sp)
            Spacer(Modifier.width(8.dp))
            (1..3).forEach { i ->
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(if (i <= stepIndex) Gold else MentorColors.TextGhost)
                )
                Spacer(Modifier.width(3.dp))
            }
        }
    }
}

@Composable
private fun Pill(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(EmeraldBright.copy(alpha = 0.10f))
            .border(1.dp, EmeraldBright, RoundedCornerShape(999.dp))
            .padding(horizontal = 12.dp, vertical = 5.dp)
    ) {
        Text(text, color = EmeraldBright, fontSize = 9.sp, letterSpacing = 4.sp)
    }
}

@Composable
private fun BrandSeal(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.width(40.dp).height(1.dp).background(GoldDeep))
        Spacer(Modifier.width(10.dp))
        Text("✦", color = Gold, fontSize = 12.sp)
        Spacer(Modifier.width(10.dp))
        Text(text, color = MentorColors.TextBody, fontSize = 11.sp, letterSpacing = 3.sp, fontStyle = FontStyle.Italic)
        Spacer(Modifier.width(10.dp))
        Text("✦", color = Gold, fontSize = 12.sp)
        Spacer(Modifier.width(10.dp))
        Box(modifier = Modifier.width(40.dp).height(1.dp).background(GoldDeep))
    }
}

private fun sealFor(step: ReflectionStep) = when (step) {
    ReflectionStep.DONE -> "murabbiy bilan baho beramiz"
    ReflectionStep.Q1_KNOWLEDGE -> "birinchi savol — bilim"
    ReflectionStep.Q2_STRENGTH -> "ikkinchi savol — kuch"
    ReflectionStep.CONFIRMED -> "tasdiqlandi · birga davom"
}

@Composable
private fun AchievementCard(
    lbl: String,
    title: String,
    durationLabel: String,
    timeRange: String,
    blockedApps: List<String>,
    extraBlockedCount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(EmeraldBright.copy(alpha = 0.06f))
            .border(1.dp, EmeraldBright.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(14.dp)
    ) {
        Text(lbl, color = EmeraldBright, fontSize = 9.sp, letterSpacing = 4.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        Text(title, color = MentorColors.TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.SemiBold, fontStyle = FontStyle.Italic)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
            Text("✓ $durationLabel", color = Gold, fontSize = 11.sp, letterSpacing = 2.sp)
            Text("∼ $timeRange", color = MentorColors.TextMuted, fontSize = 11.sp, letterSpacing = 2.sp)
        }
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            blockedApps.forEach { app ->
                BlockedPill("✓ $app")
            }
            if (extraBlockedCount > 0) BlockedPill("+ $extraBlockedCount")
        }
    }
}

@Composable
private fun BlockedPill(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(999.dp))
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(text, color = MentorColors.TextMuted, fontSize = 8.sp, letterSpacing = 1.sp)
    }
}

@Composable
private fun DoneCelebration() {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Bajarding.", color = Gold, fontSize = 30.sp, fontWeight = FontWeight.Bold, fontStyle = FontStyle.Italic)
        Spacer(Modifier.height(10.dp))
        Row {
            Text(
                "Yana 45 daqiqa ",
                color = MentorColors.TextBody,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 22.sp
            )
            Text(
                "o'zingdan ozgina kuchliroq",
                color = Gold,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 22.sp
            )
            Text(
                " bo'lding.",
                color = MentorColors.TextBody,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 22.sp
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            "Endi birga baho beramiz — miyangda nima qoldi?",
            color = MentorColors.TextBody,
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic,
            lineHeight = 22.sp
        )
    }
}

@Composable
private fun QuestionBlock(
    words: List<String>,
    emWordIndex: Int,
    prompt: String,
    answers: List<ReflectAnswer>,
    selected: String?,
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
        Column(modifier = Modifier.weight(1f)) {
            Row {
                words.forEachIndexed { i, w ->
                    Text(
                        text = if (i == words.lastIndex) w else "$w ",
                        color = if (i == emWordIndex) Gold else MentorColors.TextPrimary,
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Italic,
                        fontWeight = if (i == emWordIndex) FontWeight.SemiBold else FontWeight.Medium,
                        lineHeight = 26.sp
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
            Text(prompt, color = MentorColors.TextMuted, fontSize = 11.sp, fontStyle = FontStyle.Italic)
        }
    }
    Spacer(Modifier.height(12.dp))
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        answers.chunked(2).forEach { pair ->
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                pair.forEach { a ->
                    AnswerChip(
                        icon = a.icon,
                        label = a.label,
                        selected = a.label == selected,
                        modifier = Modifier.weight(1f),
                        onClick = { onPick(a.label) }
                    )
                }
                if (pair.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun AnswerChip(icon: String, label: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) Gold.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.03f))
            .border(1.dp, if (selected) Gold else MentorColors.TextGhost, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(icon, color = if (selected) Gold else MentorColors.TextBody, fontSize = 16.sp)
        Text(label, color = if (selected) MentorColors.TextPrimary else MentorColors.TextBody, fontSize = 12.sp, fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal)
    }
}

@Composable
private fun IdentityCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Gold.copy(alpha = 0.06f))
            .border(1.dp, Gold.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(14.dp)
    ) {
        Text("SEN — KIM BO'LDING BU LAHZADA", color = GoldDeep, fontSize = 9.sp, letterSpacing = 4.sp)
        Spacer(Modifier.height(8.dp))
        Row {
            Text("Sen — ", color = MentorColors.TextBody, fontSize = 15.sp, fontStyle = FontStyle.Italic, lineHeight = 24.sp)
            Text("o'z so'zida turgan odam", color = Gold, fontSize = 15.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.SemiBold, lineHeight = 24.sp)
            Text(".", color = MentorColors.TextBody, fontSize = 15.sp, fontStyle = FontStyle.Italic, lineHeight = 24.sp)
        }
        Text(
            "Reja qilding, qildiing. Bu — yangi sen.",
            color = MentorColors.TextBody,
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic,
            lineHeight = 22.sp
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "— birgaman, ertaga ham",
            color = Gold,
            fontSize = 11.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.fillMaxWidth(),
            textAlign = androidx.compose.ui.text.style.TextAlign.End
        )
    }
}

@Composable
private fun StatsUpdated(streakDays: Int, hoursToday: Double, sessionsThisWeek: Int) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        StatMini("+1", "$streakDays", "KUN STREAK", Modifier.weight(1f))
        StatMini("+0.75", "%.2f".format(hoursToday), "SOAT BUGUN", Modifier.weight(1f))
        StatMini("+1", "$sessionsThisWeek", "SESSIYA HAFT", Modifier.weight(1f))
    }
}

@Composable
private fun StatMini(change: String, value: String, label: String, modifier: Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(EmeraldBright.copy(alpha = 0.05f))
            .border(1.dp, EmeraldBright.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(change, color = EmeraldBright, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        Text(value, color = MentorColors.TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
        Text(label, color = MentorColors.TextMuted, fontSize = 8.sp, letterSpacing = 2.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
    }
}

@Composable
private fun MentorClosing() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .background(Gold.copy(alpha = 0.05f))
            .border(1.dp, Gold.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
            .padding(14.dp)
    ) {
        Row {
            Text(
                "Diqqat qil — har bajarilgan sessiya ",
                color = MentorColors.TextBody,
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 20.sp
            )
            Text(
                "miyangda yangi yo'l",
                color = Gold,
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp
            )
            Text(
                " ochadi. Bu — neyroplastiklik, sening foydangga.",
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
    step: ReflectionStep,
    canAdvance: Boolean,
    onAdvance: () -> Unit,
    modifier: Modifier = Modifier
) {
    val label = when (step) {
        ReflectionStep.DONE -> "Davom etamiz"
        ReflectionStep.Q1_KNOWLEDGE -> "Keyingi savol"
        ReflectionStep.Q2_STRENGTH -> "Tasdiqlash"
        ReflectionStep.CONFIRMED -> "Bosh sahifa"
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF0F0B14))
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MentorPrimaryButton(text = label, onClick = onAdvance, enabled = canAdvance)
        if (step == ReflectionStep.CONFIRMED) {
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SmallChip("Yangi sessiya", Modifier.weight(1f))
                SmallChip("Dam olaman", Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun SmallChip(text: String, modifier: Modifier) {
    Box(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = MentorColors.TextMuted, fontSize = 11.sp)
    }
}

private val q1Answers = listOf(
    ReflectAnswer("◇", "Yangi tushuncha"),
    ReflectAnswer("∽", "Eski narsa — yangicha"),
    ReflectAnswer("⚡", "O'z chegaramni bildim"),
    ReflectAnswer("✦", "Boshqa — aytay")
)

private val q2Answers = listOf(
    ReflectAnswer("◉", "Ertalabki niyat"),
    ReflectAnswer("↻", "Ko'p kunlik odat"),
    ReflectAnswer("☾", "Yaxshi uyqu"),
    ReflectAnswer("☼", "Erta turgan tongim")
)
