package uz.mentorai.focus.ui.notifications

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.mentorai.focus.ui.theme.MentorColors

private val Gold = Color(0xFFE8C77E)
private val GoldDeep = Color(0xFF8A6F44)
private val Rose = Color(0xFFC28B8B)
private val EmeraldBright = Color(0xFF6BAF7C)
private val Sky = Color(0xFF7CA8C9)

enum class NotifKind { MENTOR, ALERT, CELEBRATE, SYSTEM }

data class NotifItem(
    val kind: NotifKind,
    val type: String,
    val time: String,
    val text: String,
    val action: String? = null,
    val unread: Boolean = true
)

data class NotifGroup(val dayLabel: String, val items: List<NotifItem>)

@Composable
fun NotificationsScreen(
    groups: List<NotifGroup> = sampleGroups,
    onClose: () -> Unit = {},
    onAction: (NotifItem) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0E14))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(unreadCount = groups.flatMap { it.items }.count { it.unread }, onClose = onClose)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                groups.forEach { g ->
                    DayDivider(label = g.dayLabel)
                    g.items.forEach { n ->
                        NotifCard(item = n, onAction = onAction)
                    }
                }
            }
        }
    }
}

@Composable
private fun TopBar(unreadCount: Int, onClose: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF120E18))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "←",
            color = MentorColors.TextMuted,
            fontSize = 22.sp,
            modifier = Modifier.clickable { onClose() }
        )
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text("Bildirishnomalar", color = MentorColors.TextPrimary, fontSize = 17.sp, fontWeight = FontWeight.SemiBold)
            if (unreadCount > 0) {
                Text("$unreadCount o'qilmagan", color = Gold, fontSize = 10.sp, letterSpacing = 2.sp)
            }
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(Color.White.copy(alpha = 0.04f))
                .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(999.dp))
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            Text("hammasi o'qildi", color = MentorColors.TextMuted, fontSize = 10.sp, letterSpacing = 1.sp)
        }
    }
}

@Composable
private fun DayDivider(label: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f).height(1.dp).background(MentorColors.TextGhost))
        Spacer(Modifier.width(10.dp))
        Text(label, color = MentorColors.TextMuted, fontSize = 9.sp, letterSpacing = 3.sp)
        Spacer(Modifier.width(10.dp))
        Box(modifier = Modifier.weight(1f).height(1.dp).background(MentorColors.TextGhost))
    }
}

@Composable
private fun NotifCard(item: NotifItem, onAction: (NotifItem) -> Unit) {
    val accent = when (item.kind) {
        NotifKind.MENTOR -> Gold
        NotifKind.ALERT -> Rose
        NotifKind.CELEBRATE -> EmeraldBright
        NotifKind.SYSTEM -> Sky
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(if (item.unread) accent.copy(alpha = 0.07f) else Color.White.copy(alpha = 0.02f))
            .border(1.dp, if (item.unread) accent.copy(alpha = 0.4f) else MentorColors.TextGhost, RoundedCornerShape(10.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(accent.copy(alpha = 0.20f))
                .border(1.dp, accent, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            val icon = when (item.kind) {
                NotifKind.MENTOR -> "✦"
                NotifKind.ALERT -> "!"
                NotifKind.CELEBRATE -> "★"
                NotifKind.SYSTEM -> "i"
            }
            Text(icon, color = accent, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(item.type, color = accent, fontSize = 9.sp, letterSpacing = 2.sp, fontWeight = FontWeight.SemiBold)
                Text(item.time, color = MentorColors.TextMuted, fontSize = 9.sp)
            }
            Spacer(Modifier.height(6.dp))
            Text(
                item.text,
                color = if (item.unread) MentorColors.TextPrimary else MentorColors.TextBody,
                fontSize = 13.sp,
                fontStyle = FontStyle.Italic,
                lineHeight = 19.sp
            )
            if (item.action != null) {
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(accent.copy(alpha = 0.15f))
                        .border(1.dp, accent, RoundedCornerShape(6.dp))
                        .clickable { onAction(item) }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(item.action, color = accent, fontSize = 10.sp, letterSpacing = 2.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.width(6.dp))
                    Text("→", color = accent, fontSize = 12.sp)
                }
            }
        }
    }
}

private val sampleGroups = listOf(
    NotifGroup(
        "BUGUN · 10 MAY",
        listOf(
            NotifItem(
                kind = NotifKind.MENTOR,
                type = "MURABBIY · KUN OXIRGI",
                time = "22:14",
                text = "Bugun 3.5 soat fokusda turding. Daraxtingda yana bir halqa shakllandi. Erta yot — uyqu sifati ertangi g'alabaning yarmi.",
                action = "QISQA BAHO BERAY"
            ),
            NotifItem(
                kind = NotifKind.CELEBRATE,
                type = "YANGI YUTUQ",
                time = "17:45",
                text = "12 kun streak — yangi yuqori darajang. O'tgan oydan 4 kun ko'p."
            ),
            NotifItem(
                kind = NotifKind.MENTOR,
                type = "MURABBIY · 5 DAQIQA OLDIN",
                time = "14:25",
                text = "Qur'on darsi 5 daqiqada. Telefoning'ni qo'l ostidan uzoqroqqa qo'y — fokus shu kichik harakatdan boshlanadi."
            ),
            NotifItem(
                kind = NotifKind.ALERT,
                type = "CHEKINISH · INSTAGRAM",
                time = "11:42",
                text = "Sen bugun 1-marta bloklangan ilovaga urinding. Negotiation: sabab eshitildi, mentor inkor etdi.",
                unread = false
            ),
            NotifItem(
                kind = NotifKind.MENTOR,
                type = "MURABBIY · TONG",
                time = "06:42",
                text = "Aziz, 7s 14daq uxlading — yaxshi. Ertalabki 5 daqiqa quyosh sifatli kun va sifatli uyqu beradi. Bugun kim bo'lib uyg'onding?",
                action = "JAVOB BERAMAN",
                unread = false
            )
        )
    ),
    NotifGroup(
        "KECHA · 9 MAY · SHANBA",
        listOf(
            NotifItem(
                kind = NotifKind.MENTOR,
                type = "MURABBIY · DAM KUNI",
                time = "10:00",
                text = "Bugun shanba — dam ol. Hech qanday alarm. Telefoning seniki. Murabbiy ertaga qaytadi.",
                unread = false
            )
        )
    ),
    NotifGroup(
        "8 MAY · JUMA",
        listOf(
            NotifItem(
                kind = NotifKind.CELEBRATE,
                type = "HAFTA YAKUNI",
                time = "21:30",
                text = "Bir hafta — 47 soat fokus, 38 sessiya. O'tgan haftaga nisbatan +12%. Sen — boshqa odam bo'lyapsan.",
                action = "HAFTANI KO'RAMAN",
                unread = false
            )
        )
    )
)
