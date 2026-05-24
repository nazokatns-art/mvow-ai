package uz.mentorai.focus.data.session

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import uz.mentorai.focus.data.stats.StatsRepository
import uz.mentorai.focus.guard.policy.PolicyEngine
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionRepository @Inject constructor(
    private val dao: SessionDao,
    private val policyEngine: PolicyEngine,
    private val statsRepository: StatsRepository
) {
    val activeSession: Flow<SessionEntity?> =
        dao.observeActive().onEach { policyEngine.setActiveSession(it) }

    val recentSessions: Flow<List<SessionEntity>> = dao.observeRecent()

    suspend fun startSession(
        title: String,
        goal: String,
        durationMinutes: Int,
        blockedPackages: Set<String>
    ): SessionEntity {
        val now = System.currentTimeMillis()
        val session = SessionEntity(
            id = UUID.randomUUID().toString(),
            title = title,
            goal = goal,
            startedAtMillis = now,
            plannedEndAtMillis = now + (durationMinutes * 60_000L),
            blockedPackagesCsv = blockedPackages.joinToString(","),
            state = SessionEntity.STATE_ACTIVE
        )
        dao.insert(session)
        statsRepository.ensureToday()
        policyEngine.setActiveSession(session)
        return session
    }

    suspend fun endActiveSession(reason: String): SessionEntity? {
        val active = dao.getActive() ?: return null
        val now = System.currentTimeMillis()
        val isCompleted = now >= active.plannedEndAtMillis ||
                          reason == SessionEntity.REASON_TIMER

        val state = if (isCompleted) {
            SessionEntity.STATE_COMPLETED
        } else {
            SessionEntity.STATE_ABANDONED
        }

        dao.endSession(active.id, state, now, reason)

        if (isCompleted) {
            statsRepository.recordSessionCompleted(active.durationMinutesIfCompleted())
        } else {
            statsRepository.recordSessionAbandoned()
        }

        policyEngine.setActiveSession(null)
        return dao.getById(active.id)
    }

    suspend fun recordIntercept() {
        val active = dao.getActive() ?: return
        dao.incrementIntercepts(active.id)
        statsRepository.recordIntercept()
    }

    suspend fun recordOverrideGranted() {
        val active = dao.getActive() ?: return
        dao.update(active.copy(overrideCount = active.overrideCount + 1))
        statsRepository.recordOverrideGranted()
    }

    suspend fun loadActiveIntoEngine() {
        policyEngine.setActiveSession(dao.getActive())
    }
}

private fun SessionEntity.durationMinutesIfCompleted(): Int =
    ((plannedEndAtMillis - startedAtMillis) / 60_000L).toInt()
