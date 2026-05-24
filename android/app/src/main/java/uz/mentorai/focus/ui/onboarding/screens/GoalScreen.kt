package uz.mentorai.focus.ui.onboarding.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import uz.mentorai.focus.ui.components.MentorPrimaryButton
import uz.mentorai.focus.ui.components.MentorScreenScaffold
import uz.mentorai.focus.ui.components.MentorSectionLabel
import uz.mentorai.focus.ui.components.MentorTextField
import uz.mentorai.focus.ui.onboarding.OnboardingViewModel

@Composable
fun GoalScreen(
    viewModel: OnboardingViewModel,
    onContinue: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    var goal by remember { mutableStateOf(state.statedGoal) }

    LaunchedEffect(state.statedGoal) {
        if (goal.isEmpty() && state.statedGoal.isNotEmpty()) {
            goal = state.statedGoal
        }
    }

    val wordCount = goal.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }.size
    val isValid = wordCount >= 10
    val charLeft = 200 - goal.length

    MentorScreenScaffold(
        title = "Maqsadingni yoz.",
        subtitle = "O'z so'zlaring bilan. Aniq qil. Bu matnni Mentor sening yuzingga aytib turadi.",
        bottomActions = {
            MentorPrimaryButton(
                text = "Davom etish",
                onClick = {
                    viewModel.saveGoal(goal)
                    onContinue()
                },
                enabled = isValid
            )
        }
    ) {
        MentorSectionLabel(text = "Maqsad")
        Spacer(Modifier.height(8.dp))
        MentorTextField(
            value = goal,
            onValueChange = { if (it.length <= 200) goal = it },
            placeholder = "Misol: \"Migratsiyani juma kungacha tugatish.\"",
            minLines = 4,
            maxLines = 8,
            helperText = when {
                goal.isEmpty() -> "Kamida 10 ta so'z (hozir: 0)"
                !isValid -> "$wordCount/10 so'z. Yana ${10 - wordCount} ta so'z kerak."
                else -> "$wordCount so'z. $charLeft belgi qoldi."
            },
            isError = goal.isNotEmpty() && !isValid
        )
    }
}
