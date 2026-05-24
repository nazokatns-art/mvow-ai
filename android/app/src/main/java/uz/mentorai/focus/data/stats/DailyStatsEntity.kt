package uz.mentorai.focus.data.stats

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Bir kunlik snapshot — streak hisoblash va statistika uchun.
 * `date` — `yyyy-MM-dd` formatida.
 */
@Entity(tableName = "daily_stats")
data class DailyStatsEntity(
    @PrimaryKey val date: String,
    val sessionsCompleted: Int = 0,
    val sessionsAbandoned: Int = 0,
    val totalIntercepts: Int = 0,
    val totalOverridesGranted: Int = 0,
    val totalFocusMinutes: Int = 0,
    val streakBroken: Boolean = false
) {
    /** Bu kun streak'ga sanaladimi? */
    val countsTowardStreak: Boolean
        get() = sessionsCompleted > 0 && !streakBroken
}
