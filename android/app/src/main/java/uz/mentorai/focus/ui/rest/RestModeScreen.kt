package uz.mentorai.focus.ui.rest

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
private val Rose = Color(0xFFC28B8B)
private val Twilight = Color(0xFF6B5C8E)
private val EmeraldBright = Color(0xFF6BAF7C)

enum class RestModeKind { BURNOUT, REST_DAY, SOFT, CELEBRATE }
enum class RestEnergy { STRONG, MEDIUM, TIRED, REST }

data class RestModeContent(
    val modePill: String,
    val seal: String,
    val heroPrelude: String,
    val heroTitle: List<String>,
    val heroMessage: String,
    val heroEm: String,
    val wisdomLead: String,
    val wisdomEm: String,
    val wisdomTail: String,
    val streak: Int,
    val hours: Int,
    val sessions: Int,
    val bypass: Int,
    val activeEnergy: RestEnergy,
    val acceptLabel: String,
    val showOverride: Boolean
)

@Composable
fun RestModeScreen(
    kind: RestModeKind = RestModeKind.BURNOUT,
    userName: String = "Aziz",
    onAccept: () -> Unit = {},
    onOverride: () -> Unit = {}
) {
    var energy by remember { mutableStateOf(contentFor(kind, userName).activeEnergy) }
    val content = contentFor(kind, userName)

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
                .padding(top = 28.dp, bottom = 140.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderRow(modeLabel = content.modePill)
            Spacer(Modifier.height(18.dp))
            BrandSeal(text = content.seal)
            Spacer(Modifier.height(18.dp))
            StatsStrip(streak = content.streak, hours = content.hours, sessions = content.sessions, bypass = content.bypass)
            Spacer(Modifier.height(22.dp))
            Hero(prelude = content.heroPrelude, titleWords = content.heroTitle, message = content.heroMessage, em = content.heroEm)
            Spacer(Modifier.height(22.dp))
            ChangesGrid()
            Spacer(Modifier.height(18.dp))
            WisdomCard(lead = content.wisdomLead, em = content.wisdomEm, tail = content.wisdomTail)
            Spacer(Modifier.height(18.dp))
            EnergyRow(energy = energy, onPick = { energy = it })
        }

        if (content.acceptLabel.isNotEmpty()) {
            BottomBar(
                acceptLabel = content.acceptLabel,
                showOverride = content.showOverride,
                onAccept = onAccept,
                onOverride = onOverride,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun HeaderRow(modeLabel: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(EmeraldBright.copy(alpha = 0.10f))
                .border(1.dp, EmeraldBright, RoundedCornerShape(999.dp))
                .padding(horizontal = 12.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(EmeraldBright))
            Spacer(Modifier.width(8.dp))
            Text("MURABBIY YONDA", color = EmeraldBright, fontSize = 9.sp, letterSpacing = 4.sp)
        }
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(Twilight.copy(alpha = 0.12f))
                .border(1.dp, Twilight, RoundedCornerShape(999.dp))
                .padding(horizontal = 12.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("☾", color = Twilight, fontSize = 11.sp)
            Spacer(Modifier.width(6.dp))
            Text(modeLabel, color = Twilight, fontSize = 9.sp, letterSpacing = 3.sp)
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
        Text("❀", color = Rose, fontSize = 12.sp)
        Spacer(Modifier.width(10.dp))
        Text(text, color = MentorColors.TextBody, fontSize = 11.sp, letterSpacing = 3.sp, fontStyle = FontStyle.Italic)
        Spacer(Modifier.width(10.dp))
        Text("❀", color = Rose, fontSize = 12.sp)
        Spacer(Modifier.width(10.dp))
        Box(modifier = Modifier.width(40.dp).height(1.dp).background(GoldDeep))
    }
}

@Composable
private fun StatsStrip(streak: Int, hours: Int, sessions: Int, bypass: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(8.dp))
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        StatCell("$streak", "KUN\nSTREAK")
        StatSep()
        StatCell("$hours", "SOAT\nFOKUS · HAFT")
        StatSep()
        StatCell("$sessions", "SESSIYA\nHAFT")
        StatSep()
        StatCell("$bypass", "CHEKINISH\nHAFT", accent = Rose)
    }
}

@Composable
private fun StatCell(value: String, label: String, accent: Color = Gold) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = accent, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        Text(
            label,
            color = MentorColors.TextMuted,
            fontSize = 8.sp,
            letterSpacing = 1.5.sp,
            textAlign = TextAlign.Center,
            lineHeight = 11.sp
        )
    }
}

@Composable
private fun StatSep() {
    Box(modifier = Modifier.width(1.dp).height(28.dp).background(MentorColors.TextGhost))
}

@Composable
private fun Hero(prelude: String, titleWords: List<String>, message: String, em: String) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(prelude, color = GoldDeep, fontSize = 9.sp, letterSpacing = 4.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(12.dp))
        Row {
            titleWords.forEachIndexed { i, w ->
                Text(
                    text = if (i == titleWords.lastIndex) w else "$w ",
                    color = MentorColors.TextPrimary,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontStyle = FontStyle.Italic
                )
            }
        }
        Spacer(Modifier.height(14.dp))
        val parts = message.split("|")
        if (parts.size == 3) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    parts[0],
                    color = MentorColors.TextBody,
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        } else {
            Text(
                text = message,
                color = MentorColors.TextBody,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 22.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ChangesGrid() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ChangeCard("🔓", "ILOVALAR\nOCHIQ", Modifier.weight(1f))
        ChangeCard("🔕", "ALARMSIZ\nKUN", Modifier.weight(1f))
        ChangeCard("∅", "SESSIYA\nYO'Q", Modifier.weight(1f))
        ChangeCard("∞", "STREAK\nSAQLANADI", Modifier.weight(1f))
    }
}

@Composable
private fun ChangeCard(icon: String, label: String, modifier: Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(EmeraldBright.copy(alpha = 0.06f))
            .border(1.dp, EmeraldBright.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(vertical = 12.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(icon, fontSize = 20.sp, color = EmeraldBright)
        Text(
            label,
            color = MentorColors.TextBody,
            fontSize = 8.sp,
            letterSpacing = 1.5.sp,
            textAlign = TextAlign.Center,
            lineHeight = 11.sp
        )
    }
}

@Composable
private fun WisdomCard(lead: String, em: String, tail: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Gold.copy(alpha = 0.05f))
            .border(1.dp, Gold.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(14.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                lead,
                color = MentorColors.TextBody,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 22.sp
            )
            Text(
                em,
                color = Gold,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 22.sp
            )
            Text(
                tail,
                color = MentorColors.TextBody,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
private fun EnergyRow(energy: RestEnergy, onPick: (RestEnergy) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("BUGUNGI ENERGIYANG", color = GoldDeep, fontSize = 9.sp, letterSpacing = 4.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            EnergyChip("⚡", "KUCHLI", energy == RestEnergy.STRONG, Modifier.weight(1f)) { onPick(RestEnergy.STRONG) }
            EnergyChip("🌥", "O'RTA", energy == RestEnergy.MEDIUM, Modifier.weight(1f)) { onPick(RestEnergy.MEDIUM) }
            EnergyChip("🌙", "CHARCHADIM", energy == RestEnergy.TIRED, Modifier.weight(1f)) { onPick(RestEnergy.TIRED) }
            EnergyChip("☾", "DAM KUNI", energy == RestEnergy.REST, Modifier.weight(1f)) { onPick(RestEnergy.REST) }
        }
    }
}

@Composable
private fun EnergyChip(icon: String, name: String, selected: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) Gold.copy(alpha = 0.16f) else Color.White.copy(alpha = 0.03f))
            .border(1.dp, if (selected) Gold else MentorColors.TextGhost, RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(icon, fontSize = 18.sp)
        Text(
            name,
            color = if (selected) Gold else MentorColors.TextBody,
            fontSize = 8.5.sp,
            letterSpacing = 1.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BottomBar(
    acceptLabel: String,
    showOverride: Boolean,
    onAccept: () -> Unit,
    onOverride: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF0A0E14))
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MentorPrimaryButton(text = acceptLabel, onClick = onAccept)
        if (showOverride) {
            Spacer(Modifier.height(6.dp))
            Text(
                "yo'q, davom etaman",
                color = MentorColors.TextMuted,
                fontSize = 11.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.clickable { onOverride() }
            )
        }
    }
}

private fun contentFor(kind: RestModeKind, userName: String): RestModeContent = when (kind) {
    RestModeKind.BURNOUT -> RestModeContent(
        modePill = "DAM TAKLIFI",
        seal = "murabbiy g'amxo'rligida",
        heroPrelude = "CHARCHAGANSAN",
        heroTitle = listOf("$userName,", "dam", "ol."),
        heroMessage = "Sen 12 kun ketma-ket fokusda turding. Bugun nafas ol — ertaga yana kuchli qaytasan.",
        heroEm = "12 kun ketma-ket",
        wisdomLead = "Egilmagan kamon ",
        wisdomEm = "ko'p o'q ottiradi",
        wisdomTail = ". Egilmas — sinadi.",
        streak = 12, hours = 47, sessions = 38, bypass = 2,
        activeEnergy = RestEnergy.TIRED,
        acceptLabel = "Dam olaman",
        showOverride = true
    )
    RestModeKind.REST_DAY -> RestModeContent(
        modePill = "DAM KUNI",
        seal = "erkin kun",
        heroPrelude = "BUGUN — DAM",
        heroTitle = listOf("Sen", "erkin", "san."),
        heroMessage = "Hech qanday alarm. Hech qanday block. Telefoning to'liq seniki. Mentor faqat ertaga qaytadi.",
        heroEm = "to'liq seniki",
        wisdomLead = "Daraxt qishda dam oladi — bahorda ",
        wisdomEm = "guldek otiladi",
        wisdomTail = ".",
        streak = 12, hours = 47, sessions = 38, bypass = 2,
        activeEnergy = RestEnergy.REST,
        acceptLabel = "Dam kuni — tasdiqlayman",
        showOverride = true
    )
    RestModeKind.SOFT -> RestModeContent(
        modePill = "CHARCHOQ · -50%",
        seal = "mentor yumshoq ohangda",
        heroPrelude = "OG'IRROQ KUNDASAN",
        heroTitle = listOf("Bugun", "osonroq", "qil."),
        heroMessage = "Sessiyalaring yarmiga qisqartirildi. Block kuchsiz. Negotiation'da mentor jim. Bu ham fokus turi — ham g'amxo'rlik.",
        heroEm = "yarmiga qisqartirildi",
        wisdomLead = "Kuch — siljishda. ",
        wisdomEm = "Mayda qadam",
        wisdomTail = " ham qadam.",
        streak = 12, hours = 47, sessions = 38, bypass = 2,
        activeEnergy = RestEnergy.TIRED,
        acceptLabel = "",
        showOverride = false
    )
    RestModeKind.CELEBRATE -> RestModeContent(
        modePill = "7-KUN MUZAFFAR",
        seal = "mentor sevinishida",
        heroPrelude = "BAYRAM · BIR HAFTA",
        heroTitle = listOf("Tabriklayman.", "Sen", "qila", "olding."),
        heroMessage = "Bir hafta ketma-ket reja bo'yicha yashading. Ko'pchilik 3 kunda taslim bo'ladi. Sen — boshqa.",
        heroEm = "Ko'pchilik 3 kunda taslim",
        wisdomLead = "Har ",
        wisdomEm = "7-kun",
        wisdomTail = " — bayram. Bayram — kuchning takror tug'ilishi.",
        streak = 7, hours = 32, sessions = 24, bypass = 1,
        activeEnergy = RestEnergy.STRONG,
        acceptLabel = "Bayram — ko'rib chiqaman",
        showOverride = false
    )
}
