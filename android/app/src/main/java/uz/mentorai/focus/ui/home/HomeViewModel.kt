package uz.mentorai.focus.ui.home

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uz.mentorai.focus.data.onboarding.OnboardingRepository
import uz.mentorai.focus.data.session.SessionEntity
import uz.mentorai.focus.data.session.SessionRepository
import uz.mentorai.focus.data.stats.StatsRepository
import uz.mentorai.focus.guard.FocusGuardService
import javax.inject.Inject

data class HomeUiState(
    val statedGoal: String = "",
    val blockedPackages: Set<String> = emptySet(),
    val activeSession: SessionEntity? = null,
    val streakDays: Int = 0
) {
    val isSessionActive: Boolean get() = activeSession != null
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val onboardingRepository: OnboardingRepository,
    private val sessionRepository: SessionRepository,
    private val statsRepository: StatsRepository
) : AndroidViewModel(application) {

    val uiState: StateFlow<HomeUiState> = combine(
        onboardingRepository.state,
        sessionRepository.activeSession,
        statsRepository.currentStreak
    ) { onboarding, session, streak ->
        HomeUiState(
            statedGoal = onboarding.statedGoal,
            blockedPackages = onboarding.blockedPackages,
            activeSession = session,
            streakDays = streak
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )

    fun startSession(durationMinutes: Int) {
        val state = uiState.value
        if (state.isSessionActive) return
        if (state.blockedPackages.isEmpty()) return

        viewModelScope.launch {
            sessionRepository.startSession(
                title = "Sessiya",
                goal = state.statedGoal,
                durationMinutes = durationMinutes,
                blockedPackages = state.blockedPackages
            )
            FocusGuardService.start(getApplication())
        }
    }

    fun endSession() {
        FocusGuardService.endSession(getApplication())
        viewModelScope.launch {
            sessionRepository.endActiveSession(SessionEntity.REASON_USER)
        }
    }
}
