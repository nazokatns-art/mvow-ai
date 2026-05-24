package uz.mentorai.focus.data.stats

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyStatsDao {

    @Query("SELECT * FROM daily_stats WHERE date = :date")
    suspend fun getByDate(date: String): DailyStatsEntity?

    @Query("SELECT * FROM daily_stats ORDER BY date DESC LIMIT 60")
    fun observeRecent(): Flow<List<DailyStatsEntity>>

    @Query("SELECT * FROM daily_stats ORDER BY date DESC LIMIT 60")
    suspend fun getRecent(): List<DailyStatsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: DailyStatsEntity)

    @Query("""
        UPDATE daily_stats
        SET sessionsCompleted = sessionsCompleted + 1,
            totalFocusMinutes = totalFocusMinutes + :minutes
        WHERE date = :date
    """)
    suspend fun incrementCompleted(date: String, minutes: Int)

    @Query("UPDATE daily_stats SET sessionsAbandoned = sessionsAbandoned + 1 WHERE date = :date")
    suspend fun incrementAbandoned(date: String)

    @Query("UPDATE daily_stats SET totalIntercepts = totalIntercepts + 1 WHERE date = :date")
    suspend fun incrementIntercepts(date: String)

    @Query("UPDATE daily_stats SET totalOverridesGranted = totalOverridesGranted + 1 WHERE date = :date")
    suspend fun incrementOverrides(date: String)

    @Query("UPDATE daily_stats SET streakBroken = 1 WHERE date = :date")
    suspend fun markStreakBroken(date: String)
}
