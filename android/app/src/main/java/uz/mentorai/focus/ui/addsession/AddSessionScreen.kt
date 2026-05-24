package uz.mentorai.focus.ui.addsession

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
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.mentorai.focus.ui.components.MentorPrimaryButton
import uz.mentorai.focus.ui.theme.MentorColors

private val Gold = Color(0xFFE8C77E)
private val GoldDeep = Color(0xFF8A6F44)
private val Crimson = Color(0xFFB8334A)
private val Rose = Color(0xFFC28B8B)
private val EmeraldBright = Color(0xFF6BAF7C)

enum class Severity(val label: String, val color: Color) {
    MUQADDAS("MUQADDAS", Color(0xFFB8334A)),
    HIGH("YUQORI", Color(0xFFE8C77E)),
    MED("REJADAGI", Color(0xFF6BAF7C)),
    LOW("OPTSIYONAL", Color(0xFF7CA8C9))
}

enum class SessionType(val label: String, val icon: String, val sub: String) {
    SOLO("SOLO FOKUS", "🛡", "hammasi bloklanadi"),
    ONLINE("ONLINE DARS", "↗", "whitelist · 2 ilova")
}

@Composable
fun AddSessionScreen(
    userName: String = "Aziz",
    initialTitle: String = "",
    initialGoal: String = "",
    initialTime: String = "",
    onSave: (title: String, goal: String, time: String, severity: Severity, type: SessionType) -> Unit = { _, _, _, _, _ -> },
    onCancel: () -> Unit = {},
    onVoiceVow: () -> Unit = {}
) {
    var title by remember { mutableStateOf(initialTitle) }
    var goal by remember { mutableStateOf(initialGoal) }
    var time by remember { mutableStateOf(initialTime) }
    var severity by remember { mutableStateOf(Severity.MUQADDAS) }
    var sessionType by remember { mutableStateOf(SessionType.SOLO) }

    val ready = title.isNotBlank() && goal.isNotBlank() && time.isNotBlank()

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
                .padding(top = 28.dp, bottom = 160.dp)
        ) {
            HeaderRow(onClose = onCancel)
            Spacer(Modifier.height(18.dp))
            MentorAsk(userName = userName, ready = ready)
            Spacer(Modifier.height(18.dp))
            PreviewCard(title = title, goal = goal, time = time, severity = severity, type = sessionType)
            Spacer(Modifier.height(18.dp))
            FormField(label = "NOMI", value = title, onChange = { title = it }, placeholder = "Sessiya nomi…")
            Spacer(Modifier.height(8.dp))
            FormField(label = "MAQSAD", value = goal, onChange = { goal = it }, placeholder = "Nimaga erishasan…")
            Spacer(Modifier.height(8.dp))
            FormField(label = "VAQT", value = time, onChange = { time = it }, placeholder = "14:30 — 15:15 · har kuni")
            Spacer(Modifier.height(18.dp))
            SeverityBlock(severity = severity, onPick = { severity = it })
            Spacer(Modifier.height(14.dp))
            TypeBlock(sessionType = sessionType, onPick = { sessionType = it })
            Spacer(Modifier.height(18.dp))
            if (ready) MentorCommentary(time = time, severity = severity, type = sessionType)
        }

        BottomBar(
            enabled = ready,
            onSave = { onSave(title, goal, time, severity, sessionType) },
            onVoiceVow = onVoiceVow,
            onCancel = onCancel,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun HeaderRow(onClose: () -> Unit) {
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
            Text("✦", color = Gold, fontSize = 12.sp)
            Spacer(Modifier.width(6.dp))
            Text("YANGI SESSIYA", color = Gold, fontSize = 9.sp, letterSpacing = 3.sp)
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
private fun MentorAsk(userName: String, ready: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Gold.copy(alpha = 0.05f))
            .border(1.dp, Gold.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(modifier = Modifier.size(28.dp).clip(CircleShape).background(Gold.copy(alpha = 0.20f)))
        Spacer(Modifier.width(12.dp))
        Row {
            if (ready) {
                Text("Mana sening yo'ling. ", color = MentorColors.TextPrimary, fontSize = 18.sp, fontStyle = FontStyle.Italic, lineHeight = 26.sp)
                Text("Tasdiqlasakmi?", color = Gold, fontSize = 18.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.SemiBold, lineHeight = 26.sp)
            } else {
                Text("$userName, qaysi ", color = MentorColors.TextPrimary, fontSize = 18.sp, fontStyle = FontStyle.Italic, lineHeight = 26.sp)
                Text("yo'lni", color = Gold, fontSize = 18.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.SemiBold, lineHeight = 26.sp)
                Text(" quramiz?", color = MentorColors.TextPrimary, fontSize = 18.sp, fontStyle = FontStyle.Italic, lineHeight = 26.sp)
            }
        }
    }
}

@Composable
private fun PreviewCard(title: String, goal: String, time: String, severity: Severity, type: SessionType) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(severity.color.copy(alpha = 0.06f))
            .border(1.dp, severity.color.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
            .padding(14.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(severity.color))
                Spacer(Modifier.width(6.dp))
                Text(severity.label, color = severity.color, fontSize = 9.sp, letterSpacing = 3.sp, fontWeight = FontWeight.SemiBold)
            }
            Text(time.ifBlank { "— vaqt belgilanmagan —" }, color = if (time.isBlank()) MentorColors.TextMuted else Gold, fontSize = 11.sp, letterSpacing = 1.sp)
        }
        Spacer(Modifier.height(10.dp))
        Text(
            title.ifBlank { "— nom yo'q —" },
            color = if (title.isBlank()) MentorColors.TextMuted else MentorColors.TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            fontStyle = FontStyle.Italic
        )
        Spacer(Modifier.height(4.dp))
        Text(
            goal.ifBlank { "— maqsad yo'q —" },
            color = if (goal.isBlank()) MentorColors.TextMuted else MentorColors.TextBody,
            fontSize = 13.sp,
            fontStyle = FontStyle.Italic
        )
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetaTag("∼ ${deriveDuration(time)}")
            MetaTag("↻ HAR KUNI")
            MetaTag("${type.icon} ${type.label.split(" ")[0]}")
        }
    }
}

@Composable
private fun MetaTag(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(999.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(text, color = MentorColors.TextBody, fontSize = 9.sp, letterSpacing = 1.sp)
    }
}

private fun deriveDuration(time: String): String {
    val regex = Regex("(\\d{1,2}):(\\d{2})\\s*[—-]\\s*(\\d{1,2}):(\\d{2})")
    val m = regex.find(time) ?: return "— DAQ"
    val (h1, m1, h2, m2) = m.destructured
    val start = h1.toInt() * 60 + m1.toInt()
    val end = h2.toInt() * 60 + m2.toInt()
    val diff = (end - start).coerceAtLeast(0)
    return if (diff >= 60) "${diff / 60}S ${diff % 60}D" else "$diff DAQ"
}

@Composable
private fun FormField(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    placeholder: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = GoldDeep, fontSize = 9.sp, letterSpacing = 3.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.width(56.dp))
        Spacer(Modifier.width(10.dp))
        Box(modifier = Modifier.weight(1f)) {
            if (value.isEmpty()) {
                Text(placeholder, color = MentorColors.TextMuted, fontSize = 14.sp, fontStyle = FontStyle.Italic)
            }
            BasicTextField(
                value = value,
                onValueChange = onChange,
                textStyle = TextStyle(color = MentorColors.TextPrimary, fontSize = 14.sp),
                cursorBrush = SolidColor(Gold),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Gold.copy(alpha = 0.10f))
                .border(1.dp, Gold.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("●", color = Gold, fontSize = 12.sp)
        }
    }
}

@Composable
private fun SeverityBlock(severity: Severity, onPick: (Severity) -> Unit) {
    Column {
        Text("QANCHA MUHIM?", color = GoldDeep, fontSize = 9.sp, letterSpacing = 4.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
            Severity.entries.forEach { s ->
                val selected = s == severity
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (selected) s.color.copy(alpha = 0.18f) else Color.White.copy(alpha = 0.03f))
                        .border(1.dp, if (selected) s.color else MentorColors.TextGhost, RoundedCornerShape(4.dp))
                        .clickable { onPick(s) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        s.label,
                        color = if (selected) s.color else MentorColors.TextBody,
                        fontSize = 9.sp,
                        letterSpacing = 1.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
private fun TypeBlock(sessionType: SessionType, onPick: (SessionType) -> Unit) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        SessionType.entries.forEach { t ->
            val selected = t == sessionType
            Row(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (selected) Gold.copy(alpha = 0.12f) else Color.White.copy(alpha = 0.03f))
                    .border(1.dp, if (selected) Gold else MentorColors.TextGhost, RoundedCornerShape(8.dp))
                    .clickable { onPick(t) }
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(t.icon, fontSize = 18.sp, color = if (selected) Gold else MentorColors.TextBody)
                Spacer(Modifier.width(10.dp))
                Column {
                    Text(t.label, color = if (selected) MentorColors.TextPrimary else MentorColors.TextBody, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 1.sp)
                    Text(t.sub, color = MentorColors.TextMuted, fontSize = 9.sp, letterSpacing = 1.sp)
                }
            }
        }
    }
}

@Composable
private fun MentorCommentary(time: String, severity: Severity, type: SessionType) {
    val text = when {
        type == SessionType.ONLINE -> "Online dars — faqat shu guruhda bo'lasan. Boshqa chatlar bloklangan. Qatnashish — eshitish."
        severity == Severity.MUQADDAS -> "Yaxshi reja. ${time.take(5)} — kuchli vaqt, charchamagan paytda. Eslataman, sen tayyorlanasan."
        else -> "Yaxshi. Mentor seni bu sessiyaga eslatadi."
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Gold.copy(alpha = 0.05f))
            .border(1.dp, Gold.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(modifier = Modifier.size(22.dp).clip(CircleShape).background(Gold.copy(alpha = 0.20f)))
        Spacer(Modifier.width(10.dp))
        Text(text, color = MentorColors.TextBody, fontSize = 13.sp, fontStyle = FontStyle.Italic, lineHeight = 20.sp)
    }
}

@Composable
private fun BottomBar(
    enabled: Boolean,
    onSave: () -> Unit,
    onVoiceVow: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF0A0E14))
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MentorPrimaryButton(text = "Sessiyani muhrlash", onClick = onSave, enabled = enabled)
        Spacer(Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SecondaryChip("Ovoz va'adi qo'sh", Modifier.weight(1f), onClick = onVoiceVow)
            SecondaryChip("Bekor qilish", Modifier.weight(1f), onClick = onCancel)
        }
    }
}

@Composable
private fun SecondaryChip(text: String, modifier: Modifier, onClick: () -> Unit) {
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
