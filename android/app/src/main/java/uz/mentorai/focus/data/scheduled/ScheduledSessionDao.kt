package uz.mentorai.focus.data.scheduled

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ScheduledSessionDao {

    @Query("SELECT * FROM scheduled_sessions WHERE isActive = 1 ORDER BY startAtMillis ASC")
    fun observeAllActive(): Flow<List<ScheduledSessionEntity>>

    @Query("""
        SELECT * FROM scheduled_sessions
        WHERE isActive = 1 AND startAtMillis BETWEEN :fromMs AND :toMs
        ORDER BY startAtMillis ASC
    """)
    fun observeBetween(fromMs: Long, toMs: Long): Flow<List<ScheduledSessionEntity>>

    @Query("""
        SELECT * FROM scheduled_sessions
        WHERE isActive = 1 AND startAtMillis > :nowMs
        ORDER BY startAtMillis ASC LIMIT 1
    """)
    suspend fun getNext(nowMs: Long): ScheduledSessionEntity?

    @Query("SELECT * FROM scheduled_sessions WHERE id = :id")
    suspend fun getById(id: String): ScheduledSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ScheduledSessionEntity)

    @Update
    suspend fun update(item: ScheduledSessionEntity)

    @Delete
    suspend fun delete(item: ScheduledSessionEntity)

    @Query("UPDATE scheduled_sessions SET isActive = 0 WHERE id = :id")
    suspend fun deactivate(id: String)

    @Query("""
        UPDATE scheduled_sessions
        SET startAtMillis = :newStartMs,
            postponementsToday = postponementsToday + 1,
            lastPostponedAtMillis = :nowMs
        WHERE id = :id
    """)
    suspend fun postpone(id: String, newStartMs: Long, nowMs: Long)

    @Query("UPDATE scheduled_sessions SET postponementsToday = 0 WHERE lastPostponedAtMillis < :midnightMs")
    suspend fun resetDailyPostponements(midnightMs: Long)
}
