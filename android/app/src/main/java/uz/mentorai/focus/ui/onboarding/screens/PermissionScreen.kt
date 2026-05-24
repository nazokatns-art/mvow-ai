package uz.mentorai.focus.ui.onboarding.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.delay
import uz.mentorai.focus.core.permissions.PermissionChecker
import uz.mentorai.focus.core.permissions.PermissionMechanism
import uz.mentorai.focus.core.permissions.PermissionStep
import uz.mentorai.focus.ui.components.MentorPrimaryButton
import uz.mentorai.focus.ui.components.MentorScreenScaffold
import uz.mentorai.focus.ui.components.MentorSectionLabel
import uz.mentorai.focus.ui.theme.MentorColors

@Composable
fun PermissionScreen(
    step: PermissionStep,
    stepIndex: Int,
    totalSteps: Int,
    onContinue: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var isGranted by remember(step) {
        mutableStateOf(PermissionChecker.isGranted(context, step))
    }

    // Foydalanuvchi Settings'dan qaytib kelganda holatni qayta tekshirish
    DisposableLifecycleEffect(lifecycleOwner) { event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            isGranted = PermissionChecker.isGranted(context, step)
        }
    }

    val runtimeLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        isGranted = granted
        if (granted) onContinue()
    }

    // ACCESSIBILITY uchun maxsus polling — Settings'da yoqilganini bilish uchun
    LaunchedEffect(isGranted, step) {
        if (!isGranted &&
            (step == PermissionStep.ACCESSIBILITY ||
             step == PermissionStep.USAGE_STATS ||
             step == PermissionStep.OVERLAY)
        ) {
            // Polling — foydalanuvchi sozlamalarda turgan paytda ham aniqlaymiz
            while (!isGranted) {
                delay(500)
                isGranted = PermissionChecker.isGranted(context, step)
            }
            // Avtomatik o'tib ketmaymiz — foydalanuvchi qaytguncha kutamiz
        }
    }

    MentorScreenScaffold(
        title = step.displayName,
        subtitle = step.rationale,
        bottomActions = {
            if (isGranted) {
                MentorPrimaryButton(text = "Davom etish", onClick = onContinue)
            } else {
                MentorPrimaryButton(
                    text = "Ruxsat berish",
                    onClick = {
                        when (step.mechanism) {
                            PermissionMechanism.RUNTIME_PERMISSION -> {
                                PermissionChecker.runtimePermissionFor(step)?.let {
                                    runtimeLauncher.launch(it)
                                }
                            }
                            else -> {
                                PermissionChecker.settingsIntent(context, step)?.let {
                                    context.startActivity(it)
                                }
                            }
                        }
                    }
                )
            }
        }
    ) {
        Column {
            MentorSectionLabel(text = "Qadam $stepIndex / $totalSteps")
            Spacer(Modifier.height(24.dp))

            // Asosiy "why" matn
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(MentorColors.SurfaceSteel)
                    .border(
                        1.dp,
                        if (isGranted) MentorColors.SignalCommit else MentorColors.TextGhost,
                        RoundedCornerShape(4.dp)
                    )
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (isGranted) "BERILGAN" else "NIMA UCHUN?",
                        color = if (isGranted) MentorColors.SignalCommit else MentorColors.AccentBrass,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                }
                Text(
                    text = step.whyItMatters,
                    color = MentorColors.TextBody,
                    fontSize = 14.sp,
                    lineHeight = 22.sp
                )
            }

            // Sozlamalar uchun yo'riqnoma (settings-based ruxsatlar uchun)
            if (!isGranted && step.mechanism != PermissionMechanism.RUNTIME_PERMISSION) {
                Spacer(Modifier.height(24.dp))
                MentorSectionLabel(text = "Yo'riqnoma")
                Spacer(Modifier.height(8.dp))
                Text(
                    text = settingsHintFor(step),
                    color = MentorColors.TextMuted,
                    fontSize = 13.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

private fun settingsHintFor(step: PermissionStep): String = when (step) {
    PermissionStep.USAGE_STATS ->
        "1. \"Mentor-AI\"ni toping\n2. Switch'ni yoqing\n3. Bu ekranga qaytib keling"
    PermissionStep.OVERLAY ->
        "1. \"Mentor-AI\"ni toping\n2. \"Boshqa ilovalar ustida ko'rsatish\" ni yoqing\n3. Bu ekranga qayting"
    PermissionStep.ACCESSIBILITY ->
        "1. \"Mentor-AI: Ilova nazorati\" ni toping\n" +
        "2. Switch'ni yoqing\n" +
        "3. Tizim ogohlantirishida \"Ruxsat berish\"ni bosing\n" +
        "4. Bu ekranga qayting"
    PermissionStep.DEVICE_ADMIN ->
        "Tizim sahifasida \"Faollashtirish\" tugmasini bosing"
    else -> ""
}

@Composable
private fun DisposableLifecycleEffect(
    lifecycleOwner: androidx.lifecycle.LifecycleOwner,
    onEvent: (Lifecycle.Event) -> Unit
) {
    androidx.compose.runtime.DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event -> onEvent(event) }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}
