package uz.mentorai.focus.ui.brief

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.mentorai.focus.ui.components.MentorPrimaryButton
import uz.mentorai.focus.ui.theme.MentorColors

private val Gold = Color(0xFFE8C77E)
private val GoldDeep = Color(0xFF8A6F44)
private val EmeraldBright = Color(0xFF6BAF7C)
private val Sky = Color(0xFF7CA8C9)

enum class BriefStep { Q1_IDENTITY, Q2_PRIORITY, Q3_TINY_STEP, ACTION }

data class BriefQuestion(
    val words: List<String>,
    val emWordIndex: Int,
    val prompt: String,
    val answers: List<BriefAnswer>,
    val seal: String,
    val btn: String
)

data class BriefAnswer(val icon: String, val label: String)

@Composable
fun DailyBriefScreen(
    onDone: (identity: String, priority: String, tinyStep: String) -> Unit = { _, _, _ -> }
) {
    var step by remember { mutableStateOf(BriefStep.Q1_IDENTITY) }
    var identity by remember { mutableStateOf<String?>(null) }
    var priority by remember { mutableStateOf<String?>(null) }
    var tinyStep by remember { mutableStateOf<String?>(null) }

    val q = when (step) {
        BriefStep.Q1_IDENTITY -> q1
        BriefStep.Q2_PRIORITY -> q2
        BriefStep.Q3_TINY_STEP -> q3
        BriefStep.ACTION -> action
    }
    val currentSelection: String? = when (step) {
        BriefStep.Q1_IDENTITY -> identity
        BriefStep.Q2_PRIORITY -> priority
        BriefStep.Q3_TINY_STEP -> tinyStep
        BriefStep.ACTION -> null
    }
    val stepIndex = when (step) {
        BriefStep.Q1_IDENTITY -> 1
        BriefStep.Q2_PRIORITY -> 2
        BriefStep.Q3_TINY_STEP -> 3
        BriefStep.ACTION -> 3
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0E14))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 32.dp, bottom = 130.dp)
        ) {
            Header(stepIndex = stepIndex)
            Spacer(Modifier.height(18.dp))
            BrandSeal(text = q.seal)
            Spacer(Modifier.height(18.dp))
            NeuroStrip()
            Spacer(Modifier.height(18.dp))
            QuestionBlock(question = q)

            if (q.answers.isNotEmpty()) {
                Spacer(Modifier.height(14.dp))
                AnswerGrid(
                    answers = q.answers,
                    selectedLabel = currentSelection,
                    onPick = { picked ->
                        when (step) {
                            BriefStep.Q1_IDENTITY -> identity = picked
                            BriefStep.Q2_PRIORITY -> priority = picked
                            BriefStep.Q3_TINY_STEP -> tinyStep = picked
                            BriefStep.ACTION -> {}
                        }
                    }
                )
            }

            if (step != BriefStep.Q1_IDENTITY || identity != null) {
                Spacer(Modifier.height(14.dp))
                ReframeCard(step = step, identity = identity)
            }

            if (step == BriefStep.ACTION) {
                Spacer(Modifier.height(14.dp))
                PlanCard(priority = priority)
            }
        }

        BottomBar(
            btnLabel = q.btn,
            canAdvance = q.answers.isEmpty() || currentSelection != null,
            onAdvance = {
                step = when (step) {
                    BriefStep.Q1_IDENTITY -> BriefStep.Q2_PRIORITY
                    BriefStep.Q2_PRIORITY -> BriefStep.Q3_TINY_STEP
                    BriefStep.Q3_TINY_STEP -> BriefStep.ACTION
                    BriefStep.ACTION -> {
                        onDone(identity.orEmpty(), priority.orEmpty(), tinyStep.orEmpty())
                        step
                    }
                }
            },
            onSkip = {
                step = when (step) {
                    BriefStep.Q1_IDENTITY -> BriefStep.Q2_PRIORITY
                    BriefStep.Q2_PRIORITY -> BriefStep.Q3_TINY_STEP
                    BriefStep.Q3_TINY_STEP -> BriefStep.ACTION
                    BriefStep.ACTION -> BriefStep.ACTION
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun Header(stepIndex: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Pill(text = "☀  06:42 · TONG")
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
                        .size(if (i <= stepIndex) 6.dp else 5.dp)
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
            .background(Gold.copy(alpha = 0.10f))
            .border(1.dp, Gold, RoundedCornerShape(999.dp))
            .padding(horizontal = 12.dp, vertical = 5.dp)
    ) {
        Text(text, color = Gold, fontSize = 9.sp, letterSpacing = 4.sp)
    }
}

@Composable
private fun BrandSeal(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(1.dp)
                .background(GoldDeep)
        )
        Spacer(Modifier.width(10.dp))
        Text("✦", color = Gold, fontSize = 12.sp)
        Spacer(Modifier.width(10.dp))
        Text(
            text,
            color = MentorColors.TextBody,
            fontSize = 11.sp,
            letterSpacing = 3.sp,
            fontStyle = FontStyle.Italic
        )
        Spacer(Modifier.width(10.dp))
        Text("✦", color = Gold, fontSize = 12.sp)
        Spacer(Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(1.dp)
                .background(GoldDeep)
        )
    }
}

@Composable
private fun NeuroStrip() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        NeuroCard("☾", "7s 14d", "UYQU", Modifier.weight(1f))
        NeuroCard("☀", "5 DAQ", "TONG QUYOSHI", Modifier.weight(1f))
        NeuroCard("⚡", "82%", "ENERGIYA", Modifier.weight(1f))
    }
}

@Composable
private fun NeuroCard(icon: String, value: String, label: String, modifier: Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(icon, color = Gold, fontSize = 16.sp)
        Spacer(Modifier.height(4.dp))
        Text(value, color = MentorColors.TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        Text(label, color = MentorColors.TextMuted, fontSize = 8.sp, letterSpacing = 2.sp)
    }
}

@Composable
private fun QuestionBlock(question: BriefQuestion) {
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
            Row(modifier = Modifier.fillMaxWidth()) {
                question.words.forEachIndexed { i, w ->
                    Text(
                        text = if (i == question.words.lastIndex) w else "$w ",
                        color = if (i == question.emWordIndex) Gold else MentorColors.TextPrimary,
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Italic,
                        fontWeight = if (i == question.emWordIndex) FontWeight.SemiBold else FontWeight.Medium,
                        lineHeight = 26.sp
                    )
                }
            }
            if (question.prompt.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    question.prompt,
                    color = MentorColors.TextMuted,
                    fontSize = 11.sp,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}

@Composable
private fun AnswerGrid(
    answers: List<BriefAnswer>,
    selectedLabel: String?,
    onPick: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        answers.chunked(2).forEach { pair ->
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                pair.forEach { a ->
                    AnswerChip(
                        icon = a.icon,
                        label = a.label,
                        selected = a.label == selectedLabel,
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
private fun AnswerChip(
    icon: String,
    label: String,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) Gold.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.03f))
            .border(1.dp, if (selected) Gold else MentorColors.TextGhost, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(icon, color = if (selected) Gold else MentorColors.TextBody, fontSize = 18.sp)
        Text(
            label,
            color = if (selected) MentorColors.TextPrimary else MentorColors.TextBody,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun ReframeCard(step: BriefStep, identity: String?) {
    val (label, text) = when (step) {
        BriefStep.Q1_IDENTITY -> "MURABBIY" to (
            "Yaxshi. Kuch — his emas, yo'nalish. Sen aytding \"${identity ?: "..."}\" — endi miyangda dopamin shu so'zga moslashadi. Davom etamiz."
        )
        BriefStep.Q2_PRIORITY -> "MURABBIY" to "Aniq manzara — miya uchun ozuqa. Endi bu manzara senning prefrontalingda yondi."
        BriefStep.Q3_TINY_STEP -> "MURABBIY" to "2 daqiqalik harakat — eng katta to'siqni yengadi. Boshlash — yarmi."
        BriefStep.ACTION -> "MURABBIY" to "Bilasanmi nima yuz berdi? Sen 3 ta savolga javob berding. Miyangda 3 ta neyron yo'l yondi: identitet, vizualizatsiya, harakat. Endi qila olmasligingdan ko'ra qila olishing osonroq."
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(EmeraldBright.copy(alpha = 0.06f))
            .border(1.dp, EmeraldBright.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(14.dp)
    ) {
        Text(label, color = EmeraldBright, fontSize = 9.sp, letterSpacing = 4.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))
        Text(text, color = MentorColors.TextBody, fontSize = 13.sp, fontStyle = FontStyle.Italic, lineHeight = 20.sp)
    }
}

@Composable
private fun PlanCard(priority: String?) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Gold.copy(alpha = 0.06f))
            .border(1.dp, Gold.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(14.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("BUGUNGI ENG MUHIM", color = GoldDeep, fontSize = 9.sp, letterSpacing = 4.sp)
            Text("07:00 — 07:30", color = Gold, fontSize = 9.sp, letterSpacing = 1.sp)
        }
        Spacer(Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(Gold.copy(alpha = 0.2f))
                    .border(1.dp, Gold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("1", color = Gold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    priority ?: "Birinchi ish",
                    color = MentorColors.TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Bugun yana bir bobni o'zlashtirish",
                    color = MentorColors.TextMuted,
                    fontSize = 11.sp,
                    fontStyle = FontStyle.Italic
                )
            }
        }
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(Color.White.copy(alpha = 0.02f))
                .border(1.dp, GoldDeep, RoundedCornerShape(4.dp))
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("⚡", color = Gold, fontSize = 14.sp)
            Spacer(Modifier.width(8.dp))
            Text(
                "2 DAQIQADA: KITOBNI OL · BIRINCHI OYAT",
                color = Gold,
                fontSize = 10.sp,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun BottomBar(
    btnLabel: String,
    canAdvance: Boolean,
    onAdvance: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF0A0E14))
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MentorPrimaryButton(text = btnLabel, onClick = onAdvance, enabled = canAdvance)
        Spacer(Modifier.height(6.dp))
        Text(
            "— keyin javob beraman —",
            color = MentorColors.TextMuted,
            fontSize = 11.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.clickable { onSkip() }
        )
    }
}

private val q1 = BriefQuestion(
    words = listOf("Bugun", "kim", "bo'lib", "uyg'onding?"),
    emWordIndex = 1,
    prompt = "— ko'nglingdan kelgan birini tanla —",
    answers = listOf(
        BriefAnswer("⚡", "Kuchli, tayyor."),
        BriefAnswer("☀", "Yengil, umidli."),
        BriefAnswer("🌙", "Charchagan."),
        BriefAnswer("∼", "Bilmayman.")
    ),
    seal = "kunning niyati",
    btn = "Keyingi savol"
)

private val q2 = BriefQuestion(
    words = listOf("Kun", "tugagach,", "qaysi", "ish", "uchun", "xursand", "bo'lasan?"),
    emWordIndex = 2,
    prompt = "— miyangga aniq manzara ber —",
    answers = listOf(
        BriefAnswer("◇", "Kod loyihasini tugatsam."),
        BriefAnswer("▥", "Bir bob kitob o'qisam."),
        BriefAnswer("✦", "Sport — yugurish."),
        BriefAnswer("☼", "Oila bilan ovqatlansam.")
    ),
    seal = "ikkinchi savol",
    btn = "Tayyorman"
)

private val q3 = BriefQuestion(
    words = listOf("Hoziroq,", "2 daqiqada", "qaysi", "qadamni", "qila", "olasan?"),
    emWordIndex = 1,
    prompt = "— eng ahmoqona kichik narsa ham bo'ladi —",
    answers = listOf(
        BriefAnswer("▥", "Kitobni oldim."),
        BriefAnswer("▦", "Birinchi sahifani ochdim."),
        BriefAnswer("▧", "Quloqchin kiyaman."),
        BriefAnswer("▩", "Telefon uzoqqa qo'yaman.")
    ),
    seal = "eng kichik qadam",
    btn = "Boshlayman"
)

private val action = BriefQuestion(
    words = listOf("Mana,", "sening", "kuning.", "Boshlaymizmi?"),
    emWordIndex = 2,
    prompt = "— uchta savol, uchta javob: harakat tug'ildi —",
    answers = emptyList(),
    seal = "kuching jam bo'ldi",
    btn = "Boshlayman"
)
