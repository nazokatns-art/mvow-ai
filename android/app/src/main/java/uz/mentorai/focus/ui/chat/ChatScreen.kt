package uz.mentorai.focus.ui.chat

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
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.mentorai.focus.ui.theme.MentorColors

private val Gold = Color(0xFFE8C77E)
private val GoldDeep = Color(0xFF8A6F44)
private val EmeraldBright = Color(0xFF6BAF7C)

enum class MessageRole { USER, MENTOR }

data class ChatMessage(
    val role: MessageRole,
    val text: String,
    val timestamp: String,
    val wisdomTitle: String? = null,
    val wisdomBody: String? = null,
    val actionLabel: String? = null,
    val isVoice: Boolean = false,
    val voiceDuration: String? = null
)

@Composable
fun ChatScreen(
    onClose: () -> Unit = {},
    onMicPress: () -> Unit = {},
    onActionPress: (String) -> Unit = {}
) {
    val messages = remember { mutableStateListOf<ChatMessage>().apply { addAll(seed) } }
    var draft by remember { mutableStateOf("") }
    var typing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0810))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(onClose = onClose)
            ChatList(
                messages = messages,
                typing = typing,
                modifier = Modifier.weight(1f)
            )
            QuickPrompts(onPick = { prompt ->
                messages.add(ChatMessage(MessageRole.USER, prompt, "hozir"))
                typing = true
                scope.launch {
                    delay(1200)
                    typing = false
                    messages.add(
                        ChatMessage(
                            role = MessageRole.MENTOR,
                            text = "Tushundim. Bir savol — keyingi 5 daqiqada sen nima qila olasan?",
                            timestamp = "hozir"
                        )
                    )
                }
            })
            InputBar(
                value = draft,
                onChange = { draft = it },
                onSend = {
                    if (draft.isNotBlank()) {
                        messages.add(ChatMessage(MessageRole.USER, draft.trim(), "hozir"))
                        draft = ""
                        typing = true
                        scope.launch {
                            delay(1200)
                            typing = false
                            messages.add(
                                ChatMessage(
                                    role = MessageRole.MENTOR,
                                    text = "Eshityapman. Davom et — yana ayt.",
                                    timestamp = "hozir"
                                )
                            )
                        }
                    }
                },
                onMicPress = onMicPress
            )
        }
    }
}

@Composable
private fun TopBar(onClose: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF120E18))
            .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(0.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "←",
            color = MentorColors.TextMuted,
            fontSize = 22.sp,
            modifier = Modifier.clickable { onClose() }
        )
        Spacer(Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Gold.copy(alpha = 0.20f))
                .border(1.dp, Gold, CircleShape)
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text("Murabbiy", color = MentorColors.TextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(EmeraldBright))
                Spacer(Modifier.width(6.dp))
                Text("yonida", color = EmeraldBright, fontSize = 10.sp, letterSpacing = 2.sp)
            }
        }
    }
}

@Composable
private fun ChatList(
    messages: List<ChatMessage>,
    typing: Boolean,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    LaunchedEffect(messages.size, typing) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        DayDivider(label = "— BUGUN · 14:30 —")
        messages.forEach { msg ->
            if (msg.role == MessageRole.USER) UserMessage(msg) else MentorMessage(msg)
        }
        if (typing) TypingIndicator()
    }
}

@Composable
private fun DayDivider(label: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier.weight(1f).height(1.dp).background(MentorColors.TextGhost))
        Spacer(Modifier.width(10.dp))
        Text(label, color = MentorColors.TextMuted, fontSize = 9.sp, letterSpacing = 3.sp)
        Spacer(Modifier.width(10.dp))
        Box(modifier = Modifier.weight(1f).height(1.dp).background(MentorColors.TextGhost))
    }
}

@Composable
private fun UserMessage(msg: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Column(horizontalAlignment = Alignment.End) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp, bottomStart = 14.dp, bottomEnd = 4.dp))
                    .background(Gold.copy(alpha = 0.18f))
                    .border(1.dp, Gold.copy(alpha = 0.5f), RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp, bottomStart = 14.dp, bottomEnd = 4.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                if (msg.isVoice) {
                    VoiceBubble(duration = msg.voiceDuration ?: "0:09")
                } else {
                    Text(msg.text, color = MentorColors.TextPrimary, fontSize = 14.sp, lineHeight = 20.sp)
                }
            }
            Spacer(Modifier.height(2.dp))
            Text("${msg.timestamp}  ✓", color = MentorColors.TextMuted, fontSize = 9.sp)
        }
    }
}

@Composable
private fun VoiceBubble(duration: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Gold),
            contentAlignment = Alignment.Center
        ) {
            Text("▶", color = Color.Black, fontSize = 12.sp)
        }
        Spacer(Modifier.width(10.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
            val heights = listOf(4, 8, 14, 10, 16, 12, 6, 11, 14, 8, 4, 9, 13, 7, 5)
            heights.forEach { h ->
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(h.dp)
                        .background(Gold)
                )
            }
        }
        Spacer(Modifier.width(10.dp))
        Text(duration, color = Gold, fontSize = 11.sp)
    }
}

@Composable
private fun MentorMessage(msg: ChatMessage) {
    Row(verticalAlignment = Alignment.Bottom) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Gold.copy(alpha = 0.20f))
                .border(1.dp, Gold.copy(alpha = 0.5f), CircleShape)
        )
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 14.dp, bottomStart = 14.dp, bottomEnd = 14.dp))
                    .background(Color.White.copy(alpha = 0.04f))
                    .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(topStart = 4.dp, topEnd = 14.dp, bottomStart = 14.dp, bottomEnd = 14.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    msg.text,
                    color = MentorColors.TextPrimary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    fontStyle = FontStyle.Italic
                )
                if (msg.wisdomTitle != null && msg.wisdomBody != null) {
                    Spacer(Modifier.height(10.dp))
                    WisdomEmbed(title = msg.wisdomTitle, body = msg.wisdomBody)
                }
                if (msg.actionLabel != null) {
                    Spacer(Modifier.height(10.dp))
                    ActionCardEmbed(label = msg.actionLabel)
                }
            }
            Spacer(Modifier.height(2.dp))
            Text(msg.timestamp, color = MentorColors.TextMuted, fontSize = 9.sp)
        }
    }
}

@Composable
private fun WisdomEmbed(title: String, body: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(Gold.copy(alpha = 0.06f))
            .border(1.dp, Gold.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
            .padding(10.dp)
    ) {
        Text(title, color = GoldDeep, fontSize = 8.sp, letterSpacing = 3.sp, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(4.dp))
        Text(body, color = MentorColors.TextBody, fontSize = 12.sp, fontStyle = FontStyle.Italic, lineHeight = 18.sp)
    }
}

@Composable
private fun ActionCardEmbed(label: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(Gold.copy(alpha = 0.12f))
            .border(1.dp, Gold, RoundedCornerShape(6.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = Gold, fontSize = 12.sp, letterSpacing = 2.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
        Text("→", color = Gold, fontSize = 14.sp)
    }
}

@Composable
private fun TypingIndicator() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(Gold.copy(alpha = 0.20f))
                .border(1.dp, Gold.copy(alpha = 0.5f), CircleShape)
        )
        Spacer(Modifier.width(8.dp))
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(Color.White.copy(alpha = 0.04f))
                .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(14.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            repeat(3) {
                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Gold.copy(alpha = 0.6f)))
            }
        }
    }
}

@Composable
private fun QuickPrompts(onPick: (String) -> Unit) {
    val prompts = listOf(
        "⚡ Charchadim",
        "◇ Yo'lim qaerda?",
        "∼ Niyatim kuchsizlandi",
        "☾ Uxlay olmayapman",
        "+ Boshqa savol"
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScrollPad()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        prompts.forEach { p ->
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color.White.copy(alpha = 0.04f))
                    .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(999.dp))
                    .clickable { onPick(p) }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(p, color = MentorColors.TextBody, fontSize = 11.sp)
            }
        }
    }
}

@Composable
private fun InputBar(
    value: String,
    onChange: (String) -> Unit,
    onSend: () -> Unit,
    onMicPress: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF120E18))
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White.copy(alpha = 0.04f))
                .border(1.dp, MentorColors.TextGhost, RoundedCornerShape(20.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            if (value.isEmpty()) {
                Text(
                    "Murabbiyga ayt...",
                    color = MentorColors.TextMuted,
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic
                )
            }
            BasicTextField(
                value = value,
                onValueChange = onChange,
                textStyle = TextStyle(color = MentorColors.TextPrimary, fontSize = 13.sp),
                cursorBrush = SolidColor(Gold),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.04f))
                .border(1.dp, Gold.copy(alpha = 0.6f), CircleShape)
                .clickable { onMicPress() },
            contentAlignment = Alignment.Center
        ) {
            Text("●", color = Gold, fontSize = 16.sp)
        }
        Spacer(Modifier.width(6.dp))
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Gold)
                .clickable { onSend() },
            contentAlignment = Alignment.Center
        ) {
            Text("▶", color = Color.Black, fontSize = 14.sp)
        }
    }
}

private fun Modifier.horizontalScrollPad(): Modifier = this

private val seed = listOf(
    ChatMessage(MessageRole.USER, "Charchadim. Hech narsani boshlay olmayapman.", "14:30"),
    ChatMessage(
        MessageRole.MENTOR,
        "Tan oldim. Charchoq — signal, jazo emas. Bir savol bersam — javob berasanmi?",
        "14:31"
    ),
    ChatMessage(
        MessageRole.MENTOR,
        "Bugun qaysi paytda oxirgi marta o'zingni kuchli his qilding?",
        "14:31"
    ),
    ChatMessage(
        MessageRole.USER,
        "",
        "14:32",
        isVoice = true,
        voiceDuration = "0:09"
    ),
    ChatMessage(
        MessageRole.MENTOR,
        "Ko'rdingmi? Sen ertalabki yugurishdan keyin kuchli eding. Kuch — buyurtmalik narsa emas, lekin qayta chaqirilishi mumkin.",
        "14:32",
        wisdomTitle = "NEYROBIOLOG · DOPAMIN",
        wisdomBody = "Yugurish kortizolni tushiradi, dopaminni ko'taradi. Bu — eng oson energiya kaliti. 5-10 daqiqa kifoya."
    ),
    ChatMessage(
        MessageRole.MENTOR,
        "2 daqiqalik qadam — qaysi? Hozir.",
        "14:33",
        actionLabel = "⚡ KICHIK QADAM TANLAY"
    )
)
