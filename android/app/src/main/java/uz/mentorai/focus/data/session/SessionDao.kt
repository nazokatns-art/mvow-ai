package uz.mentorai.focus.data.session

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Query("SELECT * FROM sessions WHERE state = 'active' LIMIT 1")
    fun observeActive(): Flow<SessionEntity?>

    @Query("SELECT * FROM sessions WHERE state = 'active' LIMIT 1")
    suspend fun getActive(): SessionEntity?

    @Query("SELECT * FROM sessions WHERE id = :id")
    suspend fun getById(id: String): SessionEntity?

    @Query("SELECT * FROM sessions ORDER BY startedAtMillis DESC LIMIT 50")
    fun observeRecent(): Flow<List<SessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: SessionEntity)

    @Update
    suspend fun update(session: SessionEntity)

    @Query("UPDATE sessions SET interceptCount = interceptCount + 1 WHERE id = :id")
    suspend fun incrementIntercepts(id: String)

    @Query("""
        UPDATE sessions
        SET state = :state,
            actualEndAtMillis = :endAt,
            endReason = :reason
        WHERE id = :id
    """)
    suspend fun endSession(id: String, state: String, endAt: Long, reason: String)
}
