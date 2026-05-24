package uz.mentorai.focus.ui.session

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.mentorai.focus.data.scheduled.ScheduledSessionEntity
import uz.mentorai.focus.data.scheduled.ScheduledSessionRepository
import uz.mentorai.focus.data.session.SessionRepository
import uz.mentorai.focus.guard.FocusGuardService
import uz.mentorai.focus.guard.scheduler.SessionScheduler
import javax.inject.Inject

@HiltViewModel
class SessionStartViewModel @Inject constructor(
    application: Application,
    private val scheduledSessionRepository: ScheduledSessionRepository,
    private val sessionRepository: SessionRepository,
    private val sessionScheduler: SessionScheduler
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow<SessionStartUiState>(SessionStartUiState.Loading)
    val state: StateFlow<SessionStartUiState> = _state.asStateFlow()

    fun load(sessionId: String) {
        viewModelScope.launch {
            val scheduled = scheduledSessionRepository.getById(sessionId)
            _state.value = if (scheduled == null) {
                SessionStartUiState.NotFound
            } else {
                SessionStartUiState.Ready(
                    scheduled = scheduled,
                    canPostpone = scheduled.postponementsToday < 2
                )
            }
        }
    }

    fun startNow(sessionId: String, onStarted: () -> Unit) {
        viewModelScope.launch {
            val scheduled = scheduledSessionRepository.getById(sessionId) ?: return@launch
            sessionRepository.startSession(
                title = scheduled.title,
                goal = scheduled.goal,
                durationMinutes = scheduled.durationMinutes,
                blockedPackages = scheduled.blockedPackages
            )
            FocusGuardService.start(getApplication())
            onStarted()
        }
    }

    fun postpone(sessionId: String, minutes: Int, onPostponed: () -> Unit) {
        viewModelScope.launch {
            val updated = scheduledSessionRepository.postpone(sessionId, minutes)
            updated?.let { sessionScheduler.schedule(it) }
            onPostponed()
        }
    }
}

sealed interface SessionStartUiState {
    data object Loading : SessionStartUiState
    data object NotFound : SessionStartUiState
    data class Ready(
        val scheduled: ScheduledSessionEntity,
        val canPostpone: Boolean
    ) : SessionStartUiState
}
