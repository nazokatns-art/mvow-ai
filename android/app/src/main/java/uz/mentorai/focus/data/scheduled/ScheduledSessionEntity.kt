package uz.mentorai.focus.data.scheduled

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scheduled_sessions")
data class ScheduledSessionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val goal: String,
    val startAtMillis: Long,
    val durationMinutes: Int,
    val category: String,                  // worship, study, work, physical, family, sleep, generic
    val severity: String,                  // MAX, HIGH, MEDIUM, LOW
    val blockedPackagesCsv: String,
    val recurrenceRule: String? = null,    // "DAILY" | "WEEKDAYS" | null (one-time)
    val isActive: Boolean = true,          // foydalanuvchi tomonidan o'chirilgan bo'lishi mumkin
    val source: String = SOURCE_MANUAL,    // manual / google_calendar / outlook
    val externalId: String? = null,        // Calendar event ID (sync uchun)
    val postponementsToday: Int = 0,
    val lastPostponedAtMillis: Long? = null
) {
    val blockedPackages: Set<String>
        get() = if (blockedPackagesCsv.isBlank()) emptySet()
                else blockedPackagesCsv.split(',').toSet()

    val plannedEndAtMillis: Long
        get() = startAtMillis + durationMinutes * 60_000L

    companion object {
        const val SOURCE_MANUAL = "manual"
        const val SOURCE_GOOGLE = "google_calendar"
        const val SOURCE_OUTLOOK = "outlook"

        const val SEVERITY_MAX = "MAX"
        const val SEVERITY_HIGH = "HIGH"
        const val SEVERITY_MEDIUM = "MEDIUM"
        const val SEVERITY_LOW = "LOW"
    }
}
