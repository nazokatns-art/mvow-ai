package uz.mentorai.focus.ui.weekly

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
private val GoldFlash = Color(0xFFFFE9B5)
private val EmeraldBright = Color(0xFF6BAF7C)
private val Rose = Color(0xFFC28B8B)
private val Sky = Color(0xFF7CA8C9)

data class DayBar(val day: String, val hours: Float, val tag: BarTag = BarTag.NORMAL)
enum class BarTag { NORMAL, BEST, REST, TODAY }

@Composable
fun WeeklyReviewScreen(
    userName: String = "Aziz",
    weekNumber: Int = 19,
    days: List<DayBar> = sampleWeek,
    totalHours: Int = 47,
    sessions: Int = 38,
    bypass: Int = 2,
    completedPercent: Int = 95,
    prayerLine: String = "5/5",
    deltaPercent: Int = 12,
    longestSessionLabel: String = "2s 14daq",
    earliestWakeLabel: String = "04:42",
    streakDays: Int = 12,
    onNextWeek: () -> Unit = {},
    onShare: () -> Unit = {},
    onFullAnalysis: () -> Unit = {},
    onClose: () -> Unit = {}
) {
    val animatedHours by animateIntAsState(targetValue = totalHours, animationSpec = tween(1700), label = "hours")

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
                .padding(top = 28.dp, bottom = 140.dp)
        ) {
            HeaderRow(weekNumber = weekNumber, onClose = onClose)
            Spacer(Modifier.height(18.dp))
            TitleBlock(userName = userName)
            Spacer(Modifier.height(22.dp))
            HeroStat(value = animatedHours, sessions = sessions, deltaPercent = deltaPercent)
            Spacer(Modifier.height(22.dp))
            DayChart(days = days)
            Spacer(Modifier.height(18.dp))
            RecordsRow(longest = longestSessionLabel, earliest = earliestWakeLabel, streak = streakDays)
            Spacer(Modifier.height(18.dp))
            MentorLetter(userName = userName)
            Spacer(Modifier.height(14.dp))
            MiniStats(sessions = sessions, bypass = bypass, completedPercent = completedPercent, prayerLine = prayerLine)
        }

        BottomBar(
            onNext = onNextWeek,
            onShare = onShare,
            onFull = onFullAnalysis,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun HeaderRow(weekNumber: Int, onClose: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(Gold.copy(alpha = 0.10f))
                .border(1.dp, Gold, RoundedCornerShape(999.dp))
                .padding(horizontal = 12.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("✦", color = GoldFlash, fontSize = 12.sp)
            Spacer(Modifier.width(6.dp))
            Text("HAFTA $weekNumber · TAHLIL", color = Gold, fontSize = 9.sp, letterSpacing = 4.sp)
        }
        Box(
            modifier = Modifier
                .size(30.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.04f))
                .border(1.dp, MentorColors.TextGhost, CircleShape)
                .clickable { onClose() },
            contentAlignment = Alignment.Center
        ) {
            Text("×", color = MentorColors.TextMuted, fontSize = 16.sp)
        }
    }
}

@Composable
private fun TitleBlock(userName: String) {
    Column {
        Text("DO'STING — MURABBIY", color = GoldDeep, fontSize = 9.sp, letterSpacing = 4.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        Row {
            Text("$userName, qara ", color = MentorColors.TextPrimary, fontSize = 22.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.SemiBold)
            Text("nima qilding", color = Gold, fontSize = 22.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.SemiBold)
            Text(" bu hafta.", color = MentorColors.TextPrimary, fontSize = 22.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun HeroStat(value: Int, sessions: Int, deltaPercent: Int) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("JAMI FOKUS · BU HAFTA", color = GoldDeep, fontSize = 9.sp, letterSpacing = 4.sp)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text("$value", color = MentorColors.TextPrimary, fontSize = 80.sp, fontWeight = FontWeight.Light, letterSpacing = 2.sp)
            Spacer(Modifier.width(8.dp))
            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.padding(bottom = 18.dp)) {
                Text("SOAT", color = Gold, fontSize = 11.sp, letterSpacing = 3.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(2.dp))
                Text(
                    "↑ +$deltaPercent% o'tgan hafta",
                    color = EmeraldBright,
                    fontSize = 10.sp,
                    fontStyle = FontStyle.Italic
                )
            }
        }
        Spacer(Modifier.height(6.dp))
        Row {
            Text("Bu — ", color = MentorColors.TextBody, fontSize = 13.sp, fontStyle = FontStyle.Italic)
            Text("$sessions sessiya", color = Gold, fontSize = 13.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.SemiBold)
            Text(". Hech qachon shuncha qilmagansan.", color = MentorColors.TextBody, fontSize = 13.sp, fontStyle = FontStyle.Italic)
        }
    }
}

@Composable
private fun DayChart(days: List<DayBar>) {
    val maxH = (days.maxOf { it.hours }).coerceAtLeast(1f)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(8.dp))
            .padding(14.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("KUNLAR · SOATDA", color = GoldDeep, fontSize = 9.sp, letterSpacing = 3.sp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                LegendDot(Gold, "FOKUS")
                Spacer(Modifier.width(10.dp))
                LegendDot(GoldFlash, "EN YAXSHI")
            }
        }
        Spacer(Modifier.height(14.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            days.forEach { d -> DayBarColumn(day = d, maxH = maxH) }
        }
    }
}

@Composable
private fun LegendDot(color: Color, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
        Spacer(Modifier.width(4.dp))
        Text(text, color = MentorColors.TextMuted, fontSize = 8.sp, letterSpacing = 1.sp)
    }
}

@Composable
private fun DayBarColumn(day: DayBar, maxH: Float) {
    val color = when (day.tag) {
        BarTag.BEST -> GoldFlash
        BarTag.REST -> Sky
        BarTag.TODAY -> EmeraldBright
        BarTag.NORMAL -> Gold
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxHeight()
    ) {
        Text("%.1f".format(day.hours), color = color, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .width(20.dp)
                .weight(1f),
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(((day.hours / maxH) * 100).dp.coerceAtLeast(4.dp))
                    .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                    .background(color)
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            day.day,
            color = if (day.tag == BarTag.TODAY) EmeraldBright else MentorColors.TextMuted,
            fontSize = 9.sp,
            letterSpacing = 1.sp,
            fontWeight = if (day.tag == BarTag.TODAY) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun RecordsRow(longest: String, earliest: String, streak: Int) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        RecordCard("⏱", longest, "EN UZUN\nSESSIYA", Modifier.weight(1f))
        RecordCard("☀", earliest, "EN ERTA\nUYG'ONISH", Modifier.weight(1f))
        RecordCard("∞", "$streak KUN", "YANGI\nSTREAK", Modifier.weight(1f))
    }
}

@Composable
private fun RecordCard(icon: String, value: String, label: String, modifier: Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Gold.copy(alpha = 0.05f))
            .border(1.dp, Gold.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(vertical = 12.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(icon, fontSize = 18.sp, color = Gold)
        Text(value, color = MentorColors.TextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
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
private fun MentorLetter(userName: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Gold.copy(alpha = 0.06f))
            .border(1.dp, Gold.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(14.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("MURABBIY HAQ-QO'NI", color = GoldDeep, fontSize = 9.sp, letterSpacing = 3.sp, fontWeight = FontWeight.SemiBold)
            Text("— $userName ning do'sti", color = MentorColors.TextMuted, fontSize = 9.sp, fontStyle = FontStyle.Italic)
        }
        Spacer(Modifier.height(8.dp))
        Row {
            Text(
                "Sen ",
                color = MentorColors.TextBody,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 22.sp
            )
            Text(
                "payshanba",
                color = Gold,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 22.sp
            )
            Text(
                " kuni ",
                color = MentorColors.TextBody,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 22.sp
            )
            Text(
                "8s 18daq",
                color = Gold,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 22.sp
            )
            Text(
                " fokusda turding. Daraxtingda yana bir halqa.",
                color = MentorColors.TextBody,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 22.sp
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(
            "Shanbada dam olding — to'g'ri qilding. Daraxtning kuchi shoxchadan emas, ildizdan keladi.",
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
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun MiniStats(sessions: Int, bypass: Int, completedPercent: Int, prayerLine: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        MiniCard("$sessions", "SESSIYA", Gold, Modifier.weight(1f))
        MiniCard("$bypass", "CHEKINISH", Rose, Modifier.weight(1f))
        MiniCard("$completedPercent%", "BAJARILDI", EmeraldBright, Modifier.weight(1f))
        MiniCard(prayerLine, "NAMOZ · O'RTA", Sky, Modifier.weight(1f))
    }
}

@Composable
private fun MiniCard(value: String, label: String, accent: Color, modifier: Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(6.dp))
            .padding(vertical = 10.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(value, color = accent, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
        Text(
            label,
            color = MentorColors.TextMuted,
            fontSize = 7.5.sp,
            letterSpacing = 1.5.sp,
            textAlign = TextAlign.Center,
            lineHeight = 10.sp
        )
    }
}

@Composable
private fun BottomBar(
    onNext: () -> Unit,
    onShare: () -> Unit,
    onFull: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF0A0E14))
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MentorPrimaryButton(text = "Yangi haftani boshlayman", onClick = onNext)
        Spacer(Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SmallChip("Ulashish", Modifier.weight(1f), onShare)
            SmallChip("To'liq tahlil", Modifier.weight(1f), onFull)
        }
    }
}

@Composable
private fun SmallChip(text: String, modifier: Modifier, onClick: () -> Unit) {
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

private val sampleWeek = listOf(
    DayBar("DU", 5.5f),
    DayBar("SE", 7.2f),
    DayBar("CH", 6.0f),
    DayBar("PA", 8.3f, BarTag.BEST),
    DayBar("JU", 7.0f),
    DayBar("SH", 2.0f, BarTag.REST),
    DayBar("YA", 3.5f, BarTag.TODAY)
)
