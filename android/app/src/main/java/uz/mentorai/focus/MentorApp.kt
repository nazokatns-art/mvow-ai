package uz.mentorai.focus

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import uz.mentorai.focus.ui.LocalLanguage
import uz.mentorai.focus.ui.MainNavHost
import uz.mentorai.focus.ui.onboarding.OnboardingNavHost
import uz.mentorai.focus.ui.onboarding.OnboardingViewModel

@Composable
fun MentorApp() {
    val viewModel: OnboardingViewModel = hiltViewModel()
    val onboardingState by viewModel.state.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()

    var localComplete by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalLanguage provides currentLanguage) {
        if (onboardingState.isComplete || localComplete) {
            MainNavHost()
        } else {
            OnboardingNavHost(onFinished = { localComplete = true })
        }
    }
}
