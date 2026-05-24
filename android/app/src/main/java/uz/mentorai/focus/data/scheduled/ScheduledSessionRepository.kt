package uz.mentorai.focus.data.scheduled

import kotlinx.coroutines.flow.Flow
import uz.mentorai.focus.data.categorize.EventCategorizer
import uz.mentorai.focus.data.onboarding.OnboardingRepository
import kotlinx.coroutines.flow.first
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduledSessionRepository @Inject constructor(
    private val dao: ScheduledSessionDao,
    private val categorizer: EventCategorizer,
    private val onboardingRepository: OnboardingRepository
) {
    val activeSessions: Flow<List<ScheduledSessionEntity>> = dao.observeAllActive()

    fun observeBetween(fromMs: Long, toMs: Long): Flow<List<ScheduledSessionEntity>> =
        dao.observeBetween(fromMs, toMs)

    suspend fun getNext(nowMs: Long = System.currentTimeMillis()): ScheduledSessionEntity? =
        dao.getNext(nowMs)

    suspend fun getById(id: String): ScheduledSessionEntity? = dao.getById(id)

    suspend fun create(
        title: String,
        goal: String,
        startAtMillis: Long,
        durationMinutes: Int,
        recurrenceRule: String? = null,
        source: String = ScheduledSessionEntity.SOURCE_MANUAL,
        externalId: String? = null
    ): ScheduledSessionEntity {
        val cat = categorizer.categorize(title, goal)
        val onboarding = onboardingRepository.state.first()
        val blockedPackages = categorizer.blocklistFor(cat.category, onboarding.blockedPackages)

        val entity = ScheduledSessionEntity(
            id = UUID.randomUUID().toString(),
            title = title,
            goal = goal,
            startAtMillis = startAtMillis,
            durationMinutes = durationMinutes,
            category = cat.category,
            severity = cat.severity,
            blockedPackagesCsv = blockedPackages.joinToString(","),
            recurrenceRule = recurrenceRule,
            source = source,
            externalId = externalId
        )
        dao.insert(entity)
        return entity
    }

    suspend fun update(entity: ScheduledSessionEntity) = dao.update(entity)

    suspend fun deactivate(id: String) = dao.deactivate(id)

    suspend fun postpone(id: String, addMinutes: Int): ScheduledSessionEntity? {
        val entity = dao.getById(id) ?: return null
        val now = System.currentTimeMillis()
        val newStart = entity.startAtMillis + addMinutes * 60_000L
        dao.postpone(id, newStart, now)
        return dao.getById(id)
    }
}
