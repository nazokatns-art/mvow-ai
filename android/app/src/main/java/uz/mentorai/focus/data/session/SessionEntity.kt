package uz.mentorai.focus.data.session

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val goal: String,
    val startedAtMillis: Long,
    val plannedEndAtMillis: Long,
    val actualEndAtMillis: Long? = null,
    val blockedPackagesCsv: String,   // ',' bilan ajratilgan
    val interceptCount: Int = 0,
    val overrideCount: Int = 0,
    val state: String = STATE_ACTIVE,
    val endReason: String? = null
) {
    val blockedPackages: Set<String>
        get() = if (blockedPackagesCsv.isBlank()) emptySet()
                else blockedPackagesCsv.split(',').toSet()

    companion object {
        const val STATE_ACTIVE = "active"
        const val STATE_COMPLETED = "completed"
        const val STATE_ABANDONED = "abandoned"

        const val REASON_TIMER = "timer_expired"
        const val REASON_USER = "user_ended"
        const val REASON_FORCE_STOP = "force_stop"
    }
}
