package uz.mentorai.focus.ui.stuck

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
private val Rose = Color(0xFFC28B8B)
private val Twilight = Color(0xFF6B5C8E)

enum class StuckStep { RECOGNIZE, Q1_FEELING, Q2_PROTECTING, Q3_TINY_STEP, ACTION }

data class StuckAnswer(val icon: String, val label: String)

data class StuckState(
    val seal: String,
    val statusLabel: String,
    val recognition: List<String>?,
    val emWordIndex: Int,
    val prompt: String,
    val answers: List<StuckAnswer>,
    val reframe: String,
    val btn: String,
    val skip: String,
    val stepIndex: Int
)

@Composable
fun StuckScreen(
    stuckMinutes: Int = 14,
    onResolve: (feeling: String?, protecting: String?, tinyStep: String?) -> Unit = { _, _, _ -> },
    onDeferTalk: () -> Unit = {}
) {
    var step by remember { mutableStateOf(StuckStep.RECOGNIZE) }
    var feeling by remember { mutableStateOf<String?>(null) }
    var protecting by remember { mutableStateOf<String?>(null) }
    var tinyStep by remember { mutableStateOf<String?>(null) }

    val state = stateFor(step, stuckMinutes, feeling)
    val currentPick: String? = when (step) {
        StuckStep.RECOGNIZE, StuckStep.Q1_FEELING -> feeling
        StuckStep.Q2_PROTECTING -> protecting
        StuckStep.Q3_TINY_STEP -> tinyStep
        StuckStep.ACTION -> null
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0810))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 32.dp, bottom = 140.dp)
        ) {
            Header(label = state.statusLabel, stepIndex = state.stepIndex)
            Spacer(Modifier.height(18.dp))
            BrandSeal(text = state.seal)
            Spacer(Modifier.height(18.dp))

            state.recognition?.let { words ->
                RecognitionBlock(words = words, emIndex = state.emWordIndex)
                Spacer(Modifier.height(16.dp))
            }

            if (step == StuckStep.RECOGNIZE) {
                ActionCardSmallestStep()
                Spacer(Modifier.height(16.dp))
            }

            if (state.answers.isNotEmpty()) {
                Text(state.prompt, color = MentorColors.TextMuted, fontSize = 12.sp, fontStyle = FontStyle.Italic)
                Spacer(Modifier.height(10.dp))
                AnswerGrid(
                    answers = state.answers,
                    selected = currentPick,
                    onPick = { picked ->
                        when (step) {
                            StuckStep.RECOGNIZE -> { feeling = picked }
                            StuckStep.Q1_FEELING -> { feeling = picked }
                            StuckStep.Q2_PROTECTING -> { protecting = picked }
                            StuckStep.Q3_TINY_STEP -> { tinyStep = picked }
                            StuckStep.ACTION -> {}
                        }
                    }
                )
            }

            if (state.reframe.isNotEmpty()) {
                Spacer(Modifier.height(14.dp))
                ReframeCard(text = state.reframe)
            }
        }

        BottomBar(
            primaryLabel = state.btn,
            skipLabel = state.skip,
            canAdvance = state.answers.isEmpty() || currentPick != null,
            onAdvance = {
                step = when (step) {
                    StuckStep.RECOGNIZE -> StuckStep.Q1_FEELING
                    StuckStep.Q1_FEELING -> StuckStep.Q2_PROTECTING
                    StuckStep.Q2_PROTECTING -> StuckStep.Q3_TINY_STEP
                    StuckStep.Q3_TINY_STEP -> StuckStep.ACTION
                    StuckStep.ACTION -> { onResolve(feeling, protecting, tinyStep); StuckStep.ACTION }
                }
            },
            onSkip = onDeferTalk,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun Header(label: String, stepIndex: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(Rose.copy(alpha = 0.10f))
                .border(1.dp, Rose, RoundedCornerShape(999.dp))
                .padding(horizontal = 12.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(Rose)
            )
            Spacer(Modifier.width(8.dp))
            Text(label, color = Rose, fontSize = 9.sp, letterSpacing = 4.sp)
        }
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(Gold.copy(alpha = 0.06f))
                .border(1.dp, Gold.copy(alpha = 0.4f), RoundedCornerShape(999.dp))
                .padding(horizontal = 12.dp, vertical = 5.dp),
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

@Composable
private fun RecognitionBlock(words: List<String>, emIndex: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Twilight.copy(alpha = 0.08f))
            .border(1.dp, Twilight.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Twilight.copy(alpha = 0.30f))
        )
        Spacer(Modifier.width(12.dp))
        Row {
            words.forEachIndexed { i, w ->
                Text(
                    text = if (i == words.lastIndex) w else "$w ",
                    color = if (i == emIndex) Rose else MentorColors.TextPrimary,
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = if (i == emIndex) FontWeight.SemiBold else FontWeight.Medium,
                    lineHeight = 26.sp
                )
            }
        }
    }
}

@Composable
private fun ActionCardSmallestStep() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Gold.copy(alpha = 0.06f))
            .border(1.dp, Gold.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(14.dp)
    ) {
        Text("SEN — KIM", color = GoldDeep, fontSize = 9.sp, letterSpacing = 4.sp)
        Spacer(Modifier.height(8.dp))
        Row {
            Text("Sen — ", color = MentorColors.TextBody, fontSize = 14.sp, fontStyle = FontStyle.Italic, lineHeight = 22.sp)
            Text("turg'unlikdan chiqishni", color = Gold, fontSize = 14.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.SemiBold, lineHeight = 22.sp)
            Text(" biladigan odam.", color = MentorColors.TextBody, fontSize = 14.sp, fontStyle = FontStyle.Italic, lineHeight = 22.sp)
        }
        Spacer(Modifier.height(12.dp))
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
            Column {
                Text("2 daqiqalik qadam", color = MentorColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text("Kitobni qo'lga ol — birinchi sahifa.", color = MentorColors.TextMuted, fontSize = 11.sp, fontStyle = FontStyle.Italic)
            }
        }
    }
}

@Composable
private fun AnswerGrid(answers: List<StuckAnswer>, selected: String?, onPick: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        answers.forEach { a ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (a.label == selected) Gold.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.03f))
                    .border(1.dp, if (a.label == selected) Gold else MentorColors.TextGhost, RoundedCornerShape(8.dp))
                    .clickable { onPick(a.label) }
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(a.icon, color = if (a.label == selected) Gold else MentorColors.TextBody, fontSize = 18.sp)
                Spacer(Modifier.width(12.dp))
                Text(
                    a.label,
                    color = if (a.label == selected) MentorColors.TextPrimary else MentorColors.TextBody,
                    fontSize = 13.sp,
                    fontWeight = if (a.label == selected) FontWeight.SemiBold else FontWeight.Normal,
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }
}

@Composable
private fun ReframeCard(text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Twilight.copy(alpha = 0.10f))
            .border(1.dp, Twilight.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(14.dp)
    ) {
        Text("MURABBIY · QAYTAR", color = Twilight, fontSize = 9.sp, letterSpacing = 4.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))
        Text(text, color = MentorColors.TextBody, fontSize = 13.sp, fontStyle = FontStyle.Italic, lineHeight = 20.sp)
    }
}

@Composable
private fun BottomBar(
    primaryLabel: String,
    skipLabel: String,
    canAdvance: Boolean,
    onAdvance: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF0A0810))
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MentorPrimaryButton(text = primaryLabel, onClick = onAdvance, enabled = canAdvance)
        if (skipLabel.isNotEmpty()) {
            Spacer(Modifier.height(6.dp))
            Text(
                skipLabel,
                color = MentorColors.TextMuted,
                fontSize = 11.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.clickable { onSkip() }
            )
        }
    }
}

private fun stateFor(step: StuckStep, stuckMinutes: Int, feeling: String?): StuckState = when (step) {
    StuckStep.RECOGNIZE -> StuckState(
        seal = "murabbiy ko'rdi",
        statusLabel = "TURG'UNLIK · $stuckMinutes DAQ",
        recognition = listOf("Sen", "$stuckMinutes", "daqiqa", "ekranga", "qaragancha", "turibsan."),
        emWordIndex = 4,
        prompt = "— men hukm qilmayman, savol beraman —",
        answers = listOf(
            StuckAnswer("∼", "Tushunmadim, men shu yerdaman."),
            StuckAnswer("◇", "Boshlay olmayapman."),
            StuckAnswer("◯", "Charchadim, energiya yo'q."),
            StuckAnswer("⌖", "Ahamiyatsiz his qilyapman.")
        ),
        reframe = "",
        btn = "Tayyorman, boshlaymiz",
        skip = "— hozir gapirgim yo'q —",
        stepIndex = 0
    )
    StuckStep.Q1_FEELING -> StuckState(
        seal = "birinchi savol — nom ber",
        statusLabel = "SAVOL · 1",
        recognition = listOf("Eng", "yaqin", "his", "—", "qaysi?"),
        emWordIndex = 2,
        prompt = "— his-ni nomlash uni kichraytiradi —",
        answers = listOf(
            StuckAnswer("◐", "Qo'rquv — boshlamaslik qo'rqinchli."),
            StuckAnswer("◑", "Charchoq — bu og'ir."),
            StuckAnswer("◒", "Ma'nosizlik — nimaga kerakligini sezmayman."),
            StuckAnswer("◓", "Boshqa — aytaman.")
        ),
        reframe = "",
        btn = "Davom etamiz",
        skip = "— qaytadan o'ylayman —",
        stepIndex = 1
    )
    StuckStep.Q2_PROTECTING -> StuckState(
        seal = "ikkinchi savol — chuqurroq",
        statusLabel = "SAVOL · 2",
        recognition = listOf("Bu", "his", "sendan", "nimani", "himoya", "qilyapti?"),
        emWordIndex = 1,
        prompt = "— har his bir ehtiyojni ifoda qiladi —",
        answers = listOf(
            StuckAnswer("✺", "Mag'lubiyatdan — agar boshlasam, muvaffaqiyatsiz bo'laman."),
            StuckAnswer("✻", "Charchoqdan — energiya yetarli emas."),
            StuckAnswer("✼", "Mas'uliyatdan — ko'p narsa kutiladi."),
            StuckAnswer("✽", "Notanishdan — qaerdan boshlashni bilmayman.")
        ),
        reframe = "Yaxshi. Tan oldim. ${feeling ?: "His"} — dushman emas, signal. U shunchaki \"muhim narsa\" ekanini ko'rsatadi. Endi miyangda amigdala tinch bo'ldi — savol bilan System 2 yondi.",
        btn = "Davom etamiz",
        skip = "— biroz vaqt kerak —",
        stepIndex = 2
    )
    StuckStep.Q3_TINY_STEP -> StuckState(
        seal = "uchinchi savol — qadam",
        statusLabel = "SAVOL · 3",
        recognition = listOf("Hoziroq,", "2 daqiqada", "qaysi", "arzimas", "qadamni", "qila", "olasan?"),
        emWordIndex = 1,
        prompt = "— eng kichik harakat ham harakat —",
        answers = listOf(
            StuckAnswer("▥", "Kitobni qo'lga olish."),
            StuckAnswer("▦", "Birinchi sahifani ochish."),
            StuckAnswer("▧", "Stol oldida o'tirish."),
            StuckAnswer("▩", "Telefonni boshqa xonaga qo'yish.")
        ),
        reframe = "Sen ko'rding — qo'rquv mag'lubiyatdan emas, notanishlikdan kelyapti. Notanish — bilim bilan ochiladi. Bilim — kichik qadamdan boshlanadi.",
        btn = "Boshlayman",
        skip = "— bittasini tanladim —",
        stepIndex = 3
    )
    StuckStep.ACTION -> StuckState(
        seal = "tuman ko'tarildi",
        statusLabel = "TINIQ · TAYYORMAN",
        recognition = null,
        emWordIndex = -1,
        prompt = "",
        answers = emptyList(),
        reframe = "Bilasanmi nima yuz berdi? Sen 3 ta savolga javob berding. Miyangda 3 ta neyron yo'l yondi: tan olish, ma'no, harakat. Tuman ko'tarildi — chunki sen so'radding o'zingdan.",
        btn = "Birinchi qadamni qilaman",
        skip = "",
        stepIndex = 3
    )
}
