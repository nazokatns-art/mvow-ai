package uz.mentorai.focus.data.stats

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatsRepository @Inject constructor(
    private val dao: DailyStatsDao
) {
    val recentStats: Flow<List<DailyStatsEntity>> = dao.observeRecent()

    val currentStreak: Flow<Int> = recentStats.map { computeStreak(it) }

    suspend fun ensureToday(): String {
        val today = todayKey()
        if (dao.getByDate(today) == null) {
            dao.upsert(DailyStatsEntity(date = today))
        }
        return today
    }

    suspend fun recordSessionCompleted(durationMinutes: Int) {
        val today = ensureToday()
        dao.incrementCompleted(today, durationMinutes)
    }

    suspend fun recordSessionAbandoned() {
        val today = ensureToday()
        dao.incrementAbandoned(today)
        dao.markStreakBroken(today)
    }

    suspend fun recordIntercept() {
        val today = ensureToday()
        dao.incrementIntercepts(today)
    }

    suspend fun recordOverrideGranted() {
        val today = ensureToday()
        dao.incrementOverrides(today)
    }

    suspend fun recordStreakBroken() {
        val today = ensureToday()
        dao.markStreakBroken(today)
    }

    /**
     * Streak: bugundan boshlab orqaga qarab, har kuni `countsTowardStreak`
     * bo'lsa hisoblaymiz. Bo'sh kun (yozuv yo'q) — streak buziladi.
     */
    private fun computeStreak(stats: List<DailyStatsEntity>): Int {
        if (stats.isEmpty()) return 0
        val byDate = stats.associateBy { it.date }
        var streak = 0
        var date = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        while (true) {
            val key = date.format(formatter)
            val entry = byDate[key]
            if (entry == null) break
            // Bugun uchun: hech qanday sessiya yo'q bo'lsa ham uzilmaydi (kun davom etmoqda)
            val isToday = date == LocalDate.now()
            when {
                entry.streakBroken -> break
                entry.countsTowardStreak -> { streak++; date = date.minusDays(1) }
                isToday -> { date = date.minusDays(1) }  // bugun, hali tugallanmagan
                else -> break
            }
        }
        return streak
    }

    private fun todayKey(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(java.util.Date())
}
