package uz.mentorai.focus.ui.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uz.mentorai.focus.data.scheduled.ScheduledSessionEntity
import uz.mentorai.focus.data.scheduled.ScheduledSessionRepository
import uz.mentorai.focus.guard.scheduler.SessionScheduler
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val repository: ScheduledSessionRepository,
    private val scheduler: SessionScheduler
) : ViewModel() {

    val sessions: StateFlow<List<ScheduledSessionEntity>> =
        repository.activeSessions.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun create(
        title: String,
        goal: String,
        startAtMillis: Long,
        durationMinutes: Int,
        recurrenceRule: String? = null
    ) {
        viewModelScope.launch {
            val entity = repository.create(
                title = title,
                goal = goal,
                startAtMillis = startAtMillis,
                durationMinutes = durationMinutes,
                recurrenceRule = recurrenceRule
            )
            scheduler.schedule(entity)
        }
    }

    fun deactivate(id: String) {
        viewModelScope.launch {
            scheduler.cancel(id)
            repository.deactivate(id)
        }
    }
}
