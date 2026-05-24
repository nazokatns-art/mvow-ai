package uz.mentorai.focus.ui.online

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
private val Rose = Color(0xFFC28B8B)

data class OnlineApp(val code: String, val name: String, val color: Color)

enum class ClassMode(
    val tabLabel: String,
    val modeName: String,
    val sourceLabel: String,
    val sourceColor: Color,
    val title: String,
    val schedule: String,
    val durationLabel: String,
    val allowedApps: List<OnlineApp>,
    val mentorNote: String,
    val mentorEm: String,
    val launchLabel: String,
    val launchAppColor: Color
) {
    TELEGRAM(
        "TELEGRAM DARS", "TELEGRAM DARS",
        "TELEGRAM · @algoritm_dars_2026", Color(0xFF2AABEE),
        "Algoritm darsi · 12-mavzu", "19:00 — 20:30", "90 DAQ",
        listOf(OnlineApp("T", "TELEGRAM", Color(0xFF2AABEE)), OnlineApp("Z", "ZOOM", Color(0xFF2D8CFF))),
        "Telegram'da ", "faqat dars guruhida",
        "Telegram'ni och", Color(0xFF2AABEE)
    ),
    ZOOM(
        "ZOOM DARS", "ZOOM DARS",
        "ZOOM · MEETING 845-237-1102", Color(0xFF2D8CFF),
        "Ingliz tili · B2 darsi", "20:00 — 21:30", "90 DAQ",
        listOf(OnlineApp("Z", "ZOOM", Color(0xFF2D8CFF)), OnlineApp("T", "TELEGRAM", Color(0xFF2AABEE))),
        "", "Kamerang yoqilgan bo'lsin",
        "Zoom'ga qo'shilish", Color(0xFF2D8CFF)
    ),
    MEET(
        "G·MEET", "GOOGLE MEET",
        "GOOGLE MEET · meet/abc-defg-hij", Color(0xFF00897B),
        "Loyiha kengashi", "15:00 — 16:00", "60 DAQ",
        listOf(OnlineApp("M", "G·MEET", Color(0xFF00897B)), OnlineApp("T", "TELEGRAM", Color(0xFF2AABEE))),
        "Kengashda ", "aktiv ishtirok et",
        "Meet'ga qo'shilish", Color(0xFF00897B)
    ),
    TEAMS(
        "MS TEAMS", "MS TEAMS",
        "TEAMS · Loyiha guruhi", Color(0xFF464EB8),
        "Stand-up meeting", "09:00 — 09:30", "30 DAQ",
        listOf(OnlineApp("T", "TEAMS", Color(0xFF464EB8))),
        "Stand-up qisqa — ", "bitta jumla bilan ayt",
        "Teams'ga qo'shilish", Color(0xFF464EB8)
    ),
    DISCORD(
        "DISCORD", "DISCORD VOICE",
        "DISCORD · Coding Voice", Color(0xFF5865F2),
        "Birga ishlash · code session", "21:00 — 23:00", "120 DAQ",
        listOf(OnlineApp("D", "DISCORD", Color(0xFF5865F2)), OnlineApp("G", "GITHUB", Color(0xFF0D1117))),
        "Discord'da ", "faqat coding voice'da bo'l",
        "Discord'ni och", Color(0xFF5865F2)
    ),
    MASTERCLASS(
        "MASTERKLASS", "MASTERKLASS",
        "YOUTUBE · ALI ABDAAL — DEEP WORK", Color(0xFFFF0000),
        "Deep Work — masterklass", "18:30 — 20:00", "90 DAQ",
        listOf(OnlineApp("Y", "YOUTUBE", Color(0xFFFF0000)), OnlineApp("▥", "NOTION", Color(0xFFB5B5B5))),
        "Faqat ", "shu video'ga e'tibor",
        "YouTube'ni och", Color(0xFFFF0000)
    ),
    COURSERA(
        "COURSERA", "ONLAYN KURS",
        "COURSERA · Machine Learning · Hafta 4", Color(0xFF0056D2),
        "Coursera · ML kursi", "14:00 — 15:30", "90 DAQ",
        listOf(OnlineApp("C", "COURSERA", Color(0xFF0056D2)), OnlineApp("▥", "NOTION", Color(0xFFB5B5B5))),
        "Kurs videosini ko'rgach — ", "darhol amaliyot",
        "Kursni och", Color(0xFF0056D2)
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnlineClassScreen(
    initialMode: ClassMode = ClassMode.TELEGRAM,
    blockedApps: List<String> = listOf("INSTAGRAM", "TIKTOK", "YOUTUBE", "TWITTER", "FACEBOOK"),
    remainingTime: String = "01:07:14",
    elapsedMin: Int = 23,
    totalMin: Int = 90,
    onLaunch: (ClassMode) -> Unit = {},
    onEnd: () -> Unit = {}
) {
    var mode by remember { mutableStateOf(initialMode) }

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
            HeaderRow(mode = mode)
            Spacer(Modifier.height(14.dp))
            ModeTabs(current = mode, onPick = { mode = it })
            Spacer(Modifier.height(18.dp))
            BrandSeal(text = "murabbiy nazoratida")
            Spacer(Modifier.height(18.dp))
            SessionBlock(mode = mode)
            Spacer(Modifier.height(18.dp))
            AppsPanels(mode = mode, blockedApps = blockedApps)
            Spacer(Modifier.height(18.dp))
            MentorNoteCard(mode = mode)
            Spacer(Modifier.height(14.dp))
            TimerMini(remaining = remainingTime, elapsedMin = elapsedMin, totalMin = totalMin)
        }

        BottomBar(
            mode = mode,
            onLaunch = { onLaunch(mode) },
            onEnd = onEnd,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun HeaderRow(mode: ClassMode) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(Color.White.copy(alpha = 0.04f))
                .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(999.dp))
                .padding(horizontal = 12.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🔒", fontSize = 10.sp)
            Spacer(Modifier.width(6.dp))
            Text("KONTEKSTLI BLOK", color = MentorColors.TextBody, fontSize = 9.sp, letterSpacing = 3.sp)
        }
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(EmeraldBright.copy(alpha = 0.12f))
                .border(1.dp, EmeraldBright, RoundedCornerShape(999.dp))
                .padding(horizontal = 12.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(EmeraldBright))
            Spacer(Modifier.width(6.dp))
            Text(mode.modeName, color = EmeraldBright, fontSize = 9.sp, letterSpacing = 3.sp)
        }
    }
}

@Composable
private fun ModeTabs(current: ClassMode, onPick: (ClassMode) -> Unit) {
    val scroll = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scroll),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        ClassMode.entries.forEach { m ->
            val selected = m == current
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(if (selected) Gold.copy(alpha = 0.16f) else Color.White.copy(alpha = 0.03f))
                    .border(1.dp, if (selected) Gold else MentorColors.TextGhost, RoundedCornerShape(999.dp))
                    .clickable { onPick(m) }
                    .padding(horizontal = 12.dp, vertical = 7.dp)
            ) {
                Text(
                    m.tabLabel,
                    color = if (selected) Gold else MentorColors.TextBody,
                    fontSize = 10.sp,
                    letterSpacing = 1.sp,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                )
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
private fun SessionBlock(mode: ClassMode) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(mode.sourceColor.copy(alpha = 0.10f))
                .border(1.dp, mode.sourceColor.copy(alpha = 0.5f), RoundedCornerShape(999.dp))
                .padding(horizontal = 12.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(mode.sourceColor))
            Spacer(Modifier.width(8.dp))
            Text(mode.sourceLabel, color = mode.sourceColor, fontSize = 9.sp, letterSpacing = 2.sp)
        }
        Spacer(Modifier.height(10.dp))
        Text(mode.title, color = MentorColors.TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.SemiBold, fontStyle = FontStyle.Italic, textAlign = TextAlign.Center)
        Spacer(Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(mode.schedule, color = Gold, fontSize = 11.sp, letterSpacing = 2.sp)
            Spacer(Modifier.width(6.dp))
            Text("·", color = MentorColors.TextMuted, fontSize = 11.sp)
            Spacer(Modifier.width(6.dp))
            Text(mode.durationLabel, color = MentorColors.TextMuted, fontSize = 11.sp, letterSpacing = 2.sp)
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AppsPanels(mode: ClassMode, blockedApps: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        AppsPanel(
            label = "DARSGA RUXSAT ETILGAN",
            count = "${mode.allowedApps.size} ILOVA",
            accent = EmeraldBright,
            entries = mode.allowedApps.map { Triple(it.code, it.name, it.color) to true }
        )
        AppsPanel(
            label = "BLOKLANGAN — DIQQATSIZLIK",
            count = "${blockedApps.size} ILOVA",
            accent = Rose,
            entries = blockedApps.map { (Triple(it.first().toString(), it, Rose)) to false }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AppsPanel(
    label: String,
    count: String,
    accent: Color,
    entries: List<Pair<Triple<String, String, Color>, Boolean>>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(accent.copy(alpha = 0.05f))
            .border(1.dp, accent.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, color = accent, fontSize = 9.sp, letterSpacing = 3.sp, fontWeight = FontWeight.SemiBold)
            Text(count, color = accent.copy(alpha = 0.7f), fontSize = 9.sp, letterSpacing = 1.sp)
        }
        Spacer(Modifier.height(10.dp))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            entries.forEach { (triple, allowed) ->
                val (code, name, color) = triple
                AppPill(code = code, name = name, color = color, allowed = allowed, accent = accent)
            }
        }
    }
}

@Composable
private fun AppPill(code: String, name: String, color: Color, allowed: Boolean, accent: Color) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .border(1.dp, accent.copy(alpha = 0.4f), RoundedCornerShape(999.dp))
            .padding(horizontal = 8.dp, vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Text(code, color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(6.dp))
        Text(if (allowed) "✓" else "✕", color = accent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(4.dp))
        Text(name, color = MentorColors.TextBody, fontSize = 10.sp, letterSpacing = 1.sp)
    }
}

@Composable
private fun MentorNoteCard(mode: ClassMode) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Gold.copy(alpha = 0.06f))
            .border(1.dp, Gold.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(14.dp)
    ) {
        Text("MURABBIY ESLATMASI", color = GoldDeep, fontSize = 9.sp, letterSpacing = 4.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        Row {
            Text(
                mode.mentorNote,
                color = MentorColors.TextBody,
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 20.sp
            )
            Text(
                mode.mentorEm,
                color = Gold,
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 20.sp
            )
            Text(
                ".",
                color = MentorColors.TextBody,
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun TimerMini(remaining: String, elapsedMin: Int, totalMin: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White.copy(alpha = 0.03f))
            .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(8.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("SESSIYA QOLDI", color = GoldDeep, fontSize = 9.sp, letterSpacing = 3.sp)
            Text("$elapsedMin / $totalMin DAQ", color = MentorColors.TextMuted, fontSize = 10.sp, letterSpacing = 1.sp)
        }
        Text(remaining, color = MentorColors.TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 2.sp)
    }
}

@Composable
private fun BottomBar(
    mode: ClassMode,
    onLaunch: () -> Unit,
    onEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF0A0E14))
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MentorPrimaryButton(text = mode.launchLabel, onClick = onLaunch)
        Spacer(Modifier.height(6.dp))
        Text(
            "Sessiyani tugatish",
            color = MentorColors.TextMuted,
            fontSize = 12.sp,
            fontStyle = FontStyle.Italic,
            modifier = Modifier.clickable { onEnd() }
        )
    }
}
