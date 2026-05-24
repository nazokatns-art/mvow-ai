package uz.mentorai.focus.ui.profile

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.mentorai.focus.ui.theme.MentorColors

private val Gold = Color(0xFFE8C77E)
private val GoldDeep = Color(0xFF8A6F44)
private val GoldFlash = Color(0xFFFFE9B5)
private val EmeraldBright = Color(0xFF6BAF7C)
private val Sky = Color(0xFF7CA8C9)

enum class GrowthStage(val label: String, val meta: String, val rings: Int) {
    SEED("URUG'", "birinchi halqa endi shakllanyapti", 1),
    SAPLING("KURTAK", "ildizing chuqurroq tushyapti", 3),
    TREE("DARAXT", "har hafta yangi halqa", 5),
    OAK("ORZUDAGI", "12 hafta — chuqur ildiz", 8)
}

data class HabitChip(val icon: String, val name: String, val daysOldByUser: Int)

@Composable
fun ProfileScreen(
    userName: String = "do'stim",
    sinceLabel: String = "YO'L ENDI BOSHLANDI",
    becomingText: String = "Birinchi qadam — eng kuchlisi.",
    becomingEm: String = "Birinchi qadam",
    streakNow: Int = 0,
    bestStreak: Int = 0,
    totalSessions: Int = 0,
    totalHours: Int = 0,
    stage: GrowthStage = GrowthStage.SEED,
    habits: List<HabitChip> = emptyList(),
    relationDepth: String = "YANGI TANISHUV",
    relationText: String = "Birinchi kun. Hali sening ovozingni endi eshityapman. Birga yo'l boshlaymiz.",
    relationEm: String = "Birinchi kun",
    onBack: () -> Unit = {},
    onSettings: () -> Unit = {}
) {
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
                .padding(top = 28.dp, bottom = 60.dp)
        ) {
            HeaderRow(onBack = onBack, onSettings = onSettings)
            Spacer(Modifier.height(20.dp))
            IdentityHero(name = userName, since = sinceLabel)
            Spacer(Modifier.height(22.dp))
            BecomingCard(text = becomingText, em = becomingEm)
            Spacer(Modifier.height(18.dp))
            StatsGrid(
                streak = streakNow,
                best = bestStreak,
                sessions = totalSessions,
                hours = totalHours
            )
            Spacer(Modifier.height(18.dp))
            GrowthBlock(stage = stage)
            Spacer(Modifier.height(18.dp))
            HabitsBlock(habits = habits)
            Spacer(Modifier.height(18.dp))
            RelationBlock(depth = relationDepth, text = relationText, em = relationEm)
        }
    }
}

@Composable
private fun HeaderRow(onBack: () -> Unit, onSettings: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconBtn(text = "←", onClick = onBack)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(Gold.copy(alpha = 0.10f))
                .border(1.dp, Gold, RoundedCornerShape(999.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp)
        ) {
            Text("SEN", color = Gold, fontSize = 11.sp, letterSpacing = 6.sp, fontWeight = FontWeight.Bold)
        }
        IconBtn(text = "⚙", onClick = onSettings)
    }
}

@Composable
private fun IconBtn(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.04f))
            .border(1.dp, MentorColors.TextGhost, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = MentorColors.TextMuted, fontSize = 16.sp)
    }
}

@Composable
private fun IdentityHero(name: String, since: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(120.dp)) {
                drawCircle(
                    color = Gold.copy(alpha = 0.15f),
                    radius = size.minDimension / 2,
                    center = Offset(size.width / 2, size.height / 2)
                )
                drawCircle(
                    color = Gold.copy(alpha = 0.30f),
                    radius = size.minDimension / 2 - 8.dp.toPx(),
                    center = Offset(size.width / 2, size.height / 2),
                    style = Stroke(width = 1.dp.toPx())
                )
            }
            Box(
                modifier = Modifier
                    .size(86.dp)
                    .clip(CircleShape)
                    .background(Gold.copy(alpha = 0.20f))
                    .border(2.dp, Gold, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text("✦", color = GoldFlash, fontSize = 32.sp)
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(name, color = MentorColors.TextPrimary, fontSize = 26.sp, fontWeight = FontWeight.SemiBold, fontStyle = FontStyle.Italic)
        Spacer(Modifier.height(4.dp))
        Text(since, color = GoldDeep, fontSize = 9.sp, letterSpacing = 4.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun BecomingCard(text: String, em: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Gold.copy(alpha = 0.05f))
            .border(1.dp, Gold.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(14.dp)
    ) {
        Text("SEN — KIM BO'LIB BORMOQDASAN", color = GoldDeep, fontSize = 9.sp, letterSpacing = 4.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        Row {
            Text(em, color = Gold, fontSize = 14.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.SemiBold, lineHeight = 22.sp)
            Text(text.removePrefix(em), color = MentorColors.TextBody, fontSize = 14.sp, fontStyle = FontStyle.Italic, lineHeight = 22.sp)
        }
    }
}

@Composable
private fun StatsGrid(streak: Int, best: Int, sessions: Int, hours: Int) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        StatCard("$streak", "K", "STREAK\nHOZIR", Gold, Modifier.weight(1f))
        StatCard("$best", "K", "ENG\nUZUN", GoldFlash, Modifier.weight(1f))
        StatCard("$sessions", "", "JAMI\nSESSIYA", EmeraldBright, Modifier.weight(1f))
        StatCard("$hours", "S", "JAMI\nFOKUS", Sky, Modifier.weight(1f))
    }
}

@Composable
private fun StatCard(value: String, unit: String, label: String, accent: Color, modifier: Modifier) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(8.dp))
            .padding(vertical = 12.dp, horizontal = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(value, color = accent, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            if (unit.isNotEmpty()) {
                Text(unit, color = accent.copy(alpha = 0.6f), fontSize = 11.sp, modifier = Modifier.padding(bottom = 3.dp))
            }
        }
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
private fun GrowthBlock(stage: GrowthStage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(EmeraldBright.copy(alpha = 0.06f))
            .border(1.dp, EmeraldBright.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(14.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("DARAXTING · ${stage.label}", color = EmeraldBright, fontSize = 9.sp, letterSpacing = 3.sp, fontWeight = FontWeight.SemiBold)
            Text(stage.meta, color = MentorColors.TextMuted, fontSize = 9.sp, fontStyle = FontStyle.Italic)
        }
        Spacer(Modifier.height(14.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(140.dp)) {
                val cx = size.width / 2
                val cy = size.height / 2
                val maxR = size.minDimension * 0.42f
                repeat(stage.rings) { i ->
                    val r = maxR * ((i + 1).toFloat() / stage.rings)
                    drawCircle(
                        color = EmeraldBright.copy(alpha = 0.15f + (i.toFloat() / stage.rings) * 0.4f),
                        radius = r,
                        center = Offset(cx, cy),
                        style = Stroke(width = 1.5.dp.toPx())
                    )
                }
                drawCircle(color = EmeraldBright, radius = maxR * 0.08f, center = Offset(cx, cy))
            }
        }
    }
}

@Composable
private fun HabitsBlock(habits: List<HabitChip>) {
    Column {
        Text(
            "SHAKLANGAN ODATLAR · ${habits.size}",
            color = GoldDeep,
            fontSize = 9.sp,
            letterSpacing = 3.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(8.dp))
        if (habits.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.02f))
                    .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(8.dp))
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "·  HALI ODAT YO'Q — BIRINCHISINI QO'SH",
                    color = MentorColors.TextMuted,
                    fontSize = 10.sp,
                    letterSpacing = 2.sp
                )
            }
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                habits.forEach { h ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Gold.copy(alpha = 0.05f))
                            .border(1.dp, Gold.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(h.icon, color = Gold, fontSize = 16.sp)
                        Spacer(Modifier.width(10.dp))
                        Text(h.name, color = MentorColors.TextPrimary, fontSize = 13.sp, modifier = Modifier.weight(1f))
                        Text("${h.daysOldByUser} kun", color = Gold, fontSize = 10.sp, letterSpacing = 1.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun RelationBlock(depth: String, text: String, em: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Gold.copy(alpha = 0.05f))
            .border(1.dp, Gold.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(14.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("MURABBIY · MEN BILAN", color = GoldDeep, fontSize = 9.sp, letterSpacing = 3.sp, fontWeight = FontWeight.SemiBold)
            Text(depth, color = Gold, fontSize = 9.sp, letterSpacing = 2.sp)
        }
        Spacer(Modifier.height(8.dp))
        Row {
            Text(em, color = Gold, fontSize = 13.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.SemiBold, lineHeight = 20.sp)
            Text(text.removePrefix(em), color = MentorColors.TextBody, fontSize = 13.sp, fontStyle = FontStyle.Italic, lineHeight = 20.sp)
        }
    }
}
