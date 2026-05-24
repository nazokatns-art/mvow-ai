package uz.mentorai.focus.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * M-VoW AI rang sxemasi — "vault meets cathedral".
 * Loyiha doim dark theme — light theme yo'q (qasddan).
 *
 * Mockup'ga muvofiq: deep navy + cyan glow.
 */
object MentorColors {
    // Surfaces — deep navy/charcoal
    val SurfaceVoid = Color(0xFF0A0E14)        // Asosiy fon
    val SurfaceShadow = Color(0xFF11151D)      // Yuqori panel
    val SurfaceSteel = Color(0xFF1A1F2A)       // Kartochkalar
    val SurfaceIron = Color(0xFF232936)        // Interactive

    // Text
    val TextPrimary = Color(0xFFF5F2EC)
    val TextBody = Color(0xFFB8BBC2)
    val TextMuted = Color(0xFF6B6E76)
    val TextGhost = Color(0xFF3A3D44)

    // Accents — cyan glow (mockup)
    val AccentGlow = Color(0xFF00E5D4)         // Asosiy cyan — timer, mic, faol
    val AccentGlowDim = Color(0xFF007D75)      // Passiv cyan
    val AccentGlowEnd = Color(0xFF00B5A8)      // Gradient end
    val AccentBrass = Color(0xFFC7A36B)        // Negotiation, warn
    val AccentIron = Color(0xFFD9DCE0)         // Light alternative

    // Signals
    val SignalSuccess = Color(0xFF4A8A5C)
    val SignalWarn = Color(0xFFC7A36B)
    val SignalFail = Color(0xFF8C2A2A)
    val SignalAlarm = Color(0xFFE63946)
    val SignalCommit = Color(0xFF4A8A5C)
}

object MentorTypography {
    val display = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        letterSpacing = 2.sp
    )

    val timer = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Medium,
        fontSize = 56.sp,
        letterSpacing = 2.sp
    )

    val body = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )

    val label = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        letterSpacing = 2.sp
    )
}

private val DisciplineColorScheme = darkColorScheme(
    primary = MentorColors.AccentGlow,
    onPrimary = MentorColors.SurfaceVoid,
    secondary = MentorColors.AccentBrass,
    background = MentorColors.SurfaceVoid,
    onBackground = MentorColors.TextPrimary,
    surface = MentorColors.SurfaceSteel,
    onSurface = MentorColors.TextPrimary,
    surfaceVariant = MentorColors.SurfaceIron,
    onSurfaceVariant = MentorColors.TextBody,
    error = MentorColors.SignalFail,
    outline = MentorColors.TextMuted,
    outlineVariant = MentorColors.TextGhost
)

@Composable
fun MentorTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DisciplineColorScheme,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}
