package uz.mentorai.focus.ui.onboarding

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uz.mentorai.focus.core.permissions.PermissionStep
import uz.mentorai.focus.ui.onboarding.screens.AppSelectionScreen
import uz.mentorai.focus.ui.onboarding.screens.CompleteScreen
import uz.mentorai.focus.ui.onboarding.screens.GoalScreen
import uz.mentorai.focus.ui.onboarding.screens.IntroScreen
import uz.mentorai.focus.ui.onboarding.screens.LanguageScreen
import uz.mentorai.focus.ui.onboarding.screens.PermissionScreen
import uz.mentorai.focus.ui.onboarding.screens.VoiceCommitmentScreen
import uz.mentorai.focus.ui.onboarding.screens.WelcomeScreen

object OnboardingRoutes {
    const val LANGUAGE = "language"
    const val WELCOME = "welcome"
    const val INTRO = "intro"
    const val GOAL = "goal"
    const val VOICE = "voice"
    const val PERMISSION_PREFIX = "permission"
    const val APPS = "apps"
    const val COMPLETE = "complete"

    fun permission(step: PermissionStep) = "$PERMISSION_PREFIX/${step.name}"
}

@Composable
fun OnboardingNavHost(
    onFinished: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    val viewModel: OnboardingViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = OnboardingRoutes.LANGUAGE
    ) {
        composable(OnboardingRoutes.LANGUAGE) {
            LanguageScreen(
                viewModel = viewModel,
                onContinue = { navController.navigate(OnboardingRoutes.WELCOME) }
            )
        }

        composable(OnboardingRoutes.WELCOME) {
            WelcomeScreen(
                onContinue = { navController.navigate(OnboardingRoutes.INTRO) }
            )
        }

        composable(OnboardingRoutes.INTRO) {
            IntroScreen(
                onContinue = { navController.navigate(OnboardingRoutes.GOAL) }
            )
        }

        composable(OnboardingRoutes.GOAL) {
            GoalScreen(
                viewModel = viewModel,
                onContinue = { navController.navigate(OnboardingRoutes.VOICE) }
            )
        }

        composable(OnboardingRoutes.VOICE) {
            VoiceCommitmentScreen(
                viewModel = viewModel,
                onContinue = {
                    navController.navigate(
                        OnboardingRoutes.permission(PermissionStep.ORDERED.first())
                    )
                }
            )
        }

        PermissionStep.ORDERED.forEachIndexed { index, step ->
            composable(OnboardingRoutes.permission(step)) {
                val nextRoute = PermissionStep.ORDERED.getOrNull(index + 1)
                    ?.let { OnboardingRoutes.permission(it) }
                    ?: OnboardingRoutes.APPS

                PermissionScreen(
                    step = step,
                    stepIndex = index + 1,
                    totalSteps = PermissionStep.ORDERED.size,
                    onContinue = { navController.navigate(nextRoute) }
                )
            }
        }

        composable(OnboardingRoutes.APPS) {
            AppSelectionScreen(
                viewModel = viewModel,
                onContinue = { navController.navigate(OnboardingRoutes.COMPLETE) }
            )
        }

        composable(OnboardingRoutes.COMPLETE) {
            CompleteScreen(
                viewModel = viewModel,
                onFinish = {
                    viewModel.finishOnboarding()
                    onFinished()
                }
            )
        }
    }
}
