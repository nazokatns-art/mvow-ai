package uz.mentorai.focus.ui.urgent

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.mentorai.focus.ui.components.MentorPrimaryButton
import uz.mentorai.focus.ui.theme.MentorColors
import java.util.Calendar
import kotlin.math.cos
import kotlin.math.sin

private val Gold = Color(0xFFE8C77E)
private val GoldDeep = Color(0xFF8A6F44)
private val Crimson = Color(0xFFB8334A)

/**
 * Shoshilinch vaqt — sessiya ichida mentordan vaqt so'rash.
 * Mirrors docs/v2/preview/urgent-time.html.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun UrgentTimeScreen(
    onGrant: (minutes: Int) -> Unit = {},
    onCancel: () -> Unit = {}
) {
    var minutes by remember { mutableIntStateOf(15) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF14060B))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(top = 28.dp, bottom = 140.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderRow()
            Spacer(Modifier.height(18.dp))
            BrandSeal(text = "murabbiy so'raydi")
            Spacer(Modifier.height(18.dp))
            Question()
            Spacer(Modifier.height(20.dp))
            TimeDial(minutes = minutes)
            Spacer(Modifier.height(14.dp))
            QuickChips(current = minutes, onPick = { minutes = it })
            Spacer(Modifier.height(14.dp))
            ReasonInput()
            Spacer(Modifier.height(14.dp))
            ResumeAt(minutes = minutes)
        }

        BottomBar(
            minutes = minutes,
            onGrant = { onGrant(minutes) },
            onCancel = onCancel,
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
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(Crimson.copy(alpha = 0.12f))
                .border(1.dp, Crimson, RoundedCornerShape(999.dp))
                .padding(horizontal = 12.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Crimson))
            Spacer(Modifier.width(8.dp))
            Text("SHOSHILINCH", color = Crimson, fontSize = 9.sp, letterSpacing = 4.sp)
        }
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(Color.White.copy(alpha = 0.03f))
                .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(999.dp))
                .padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.width(3.dp).height(8.dp).background(MentorColors.TextMuted))
            Spacer(Modifier.width(3.dp))
            Box(modifier = Modifier.width(3.dp).height(8.dp).background(MentorColors.TextMuted))
            Spacer(Modifier.width(6.dp))
            Text("SESSIYA PAUZA", color = MentorColors.TextMuted, fontSize = 9.sp, letterSpacing = 3.sp)
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
private fun Question() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "SEN AYTDING — \"SHOSHILINCH ISHIM CHIQDI\"",
            color = GoldDeep,
            fontSize = 9.sp,
            letterSpacing = 3.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(8.dp))
        Row {
            Text(
                "Yaxshi. ",
                color = MentorColors.TextPrimary,
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Medium
            )
            Text(
                "Qancha vaqt",
                color = Gold,
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                " kerak?",
                color = MentorColors.TextPrimary,
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun TimeDial(minutes: Int) {
    val target = (minutes.coerceIn(0, 60)) / 60f
    val animated by animateFloatAsState(target, tween(400), label = "dial")
    Box(modifier = Modifier.size(240.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = 6.dp.toPx()
            val inset = stroke / 2
            drawCircle(color = Gold.copy(alpha = 0.12f), radius = (size.minDimension - stroke) / 2, style = Stroke(width = stroke))
            drawArc(
                color = Gold,
                startAngle = -90f,
                sweepAngle = 360f * animated,
                useCenter = false,
                style = Stroke(width = stroke),
                topLeft = Offset(inset, inset),
                size = Size(size.width - stroke, size.height - stroke)
            )
            // Tick marks
            val radius = (size.minDimension / 2) - stroke
            for (i in 0..12) {
                val angle = (i * 30f - 90f) * (3.14159f / 180f)
                val major = i % 3 == 0
                val outerR = radius - 4.dp.toPx()
                val innerR = if (major) radius - 14.dp.toPx() else radius - 10.dp.toPx()
                val cx = size.width / 2
                val cy = size.height / 2
                val x1 = cx + cos(angle) * outerR
                val y1 = cy + sin(angle) * outerR
                val x2 = cx + cos(angle) * innerR
                val y2 = cy + sin(angle) * innerR
                drawLine(
                    color = if (major) Gold.copy(alpha = 0.6f) else MentorColors.TextGhost,
                    start = Offset(x1, y1),
                    end = Offset(x2, y2),
                    strokeWidth = if (major) 1.5.dp.toPx() else 1.dp.toPx()
                )
            }
            // Cursor
            val cursorAngle = (animated * 360f - 90f) * (3.14159f / 180f)
            val cursorR = radius - 1.dp.toPx()
            val cx = size.width / 2 + cos(cursorAngle) * cursorR
            val cy = size.height / 2 + sin(cursorAngle) * cursorR
            drawCircle(color = Gold, radius = 8.dp.toPx(), center = Offset(cx, cy))
            drawCircle(color = Color(0xFFFFE9B5), radius = 4.dp.toPx(), center = Offset(cx, cy))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("$minutes", color = MentorColors.TextPrimary, fontSize = 64.sp, fontWeight = FontWeight.Light)
            Text("DAQIQA", color = Gold, fontSize = 11.sp, letterSpacing = 4.sp, fontWeight = FontWeight.SemiBold)
        }
        // Number labels at 12/3/6/9
        DialLabel("60", Alignment.TopCenter)
        DialLabel("15", Alignment.CenterEnd)
        DialLabel("30", Alignment.BottomCenter)
        DialLabel("45", Alignment.CenterStart)
    }
}

@Composable
private fun DialLabel(text: String, alignment: Alignment) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = alignment) {
        Text(
            text,
            color = MentorColors.TextMuted,
            fontSize = 10.sp,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(20.dp)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun QuickChips(current: Int, onPick: (Int) -> Unit) {
    val chips = listOf(5, 10, 15, 30, 45, 60)
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        chips.forEach { m ->
            val selected = m == current
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(if (selected) Gold.copy(alpha = 0.18f) else Color.White.copy(alpha = 0.03f))
                    .border(1.dp, if (selected) Gold else MentorColors.TextGhost, RoundedCornerShape(999.dp))
                    .clickable { onPick(m) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = if (m < 60) "$m daq" else "1 soat",
                    color = if (selected) Gold else MentorColors.TextBody,
                    fontSize = 11.sp,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun ReasonInput() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Gold.copy(alpha = 0.12f))
                .border(1.dp, Gold.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("●", color = Gold, fontSize = 14.sp)
        }
        Spacer(Modifier.width(12.dp))
        Text(
            "Sabab ayt (ixtiyoriy)…",
            color = MentorColors.TextMuted,
            fontSize = 13.sp,
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
private fun ResumeAt(minutes: Int) {
    val cal = Calendar.getInstance().apply { add(Calendar.MINUTE, minutes) }
    val resume = "%02d:%02d".format(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(Gold.copy(alpha = 0.05f))
            .border(1.dp, GoldDeep.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("QAYTADAN BOSHLANADI", color = GoldDeep, fontSize = 9.sp, letterSpacing = 3.sp)
        Text(resume, color = Gold, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 2.sp)
    }
}

@Composable
private fun BottomBar(
    minutes: Int,
    onGrant: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val label = if (minutes < 60) "$minutes daqiqaga ruxsat ber"
                else {
                    val h = minutes / 60
                    val m = minutes % 60
                    if (m == 0) "$h soat ruxsat ber" else "${h}s $m daq ruxsat ber"
                }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF14060B))
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MentorPrimaryButton(text = label, onClick = onGrant)
        Spacer(Modifier.height(6.dp))
        Text(
            "Bekor qil — sessiyaga qaytaman",
            color = MentorColors.TextMuted,
            fontSize = 12.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.clickable { onCancel() }
        )
    }
}
