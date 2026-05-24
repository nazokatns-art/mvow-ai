package uz.mentorai.focus.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.mentorai.focus.data.scheduled.ScheduledSessionDao
import uz.mentorai.focus.data.scheduled.ScheduledSessionEntity
import uz.mentorai.focus.data.session.SessionDao
import uz.mentorai.focus.data.session.SessionEntity
import uz.mentorai.focus.data.stats.DailyStatsDao
import uz.mentorai.focus.data.stats.DailyStatsEntity

@Database(
    entities = [
        SessionEntity::class,
        ScheduledSessionEntity::class,
        DailyStatsEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class MentorDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun scheduledSessionDao(): ScheduledSessionDao
    abstract fun dailyStatsDao(): DailyStatsDao

    companion object {
        const val DB_NAME = "mentor.db"
    }
}
