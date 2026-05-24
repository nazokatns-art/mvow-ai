package uz.mentorai.focus.data.categorize

import uz.mentorai.focus.data.scheduled.ScheduledSessionEntity
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Vazifa nomidan kategoriya va severity'ni aniqlaydi.
 *
 * MVP — keyword-based. Sprint 3'da Anthropic Haiku 4.5'ga ko'chiriladi
 * (qoldiq fallback sifatida saqlab qoladi).
 */
@Singleton
class EventCategorizer @Inject constructor() {

    fun categorize(title: String, description: String? = null): Categorization {
        val text = (title + " " + (description ?: "")).lowercase()

        for (rule in RULES) {
            if (rule.matches(text)) return rule.categorization
        }

        return Categorization(category = "generic", severity = ScheduledSessionEntity.SEVERITY_MEDIUM)
    }

    fun blocklistFor(category: String, allBlockedPackages: Set<String>): Set<String> {
        return when (category) {
            "worship" -> allBlockedPackages // Hammasi
            "study" -> allBlockedPackages.filterNot { it in WHITELIST_STUDY }.toSet()
            "physical" -> allBlockedPackages.filterNot { it in WHITELIST_PHYSICAL }.toSet()
            "work" -> allBlockedPackages.filterNot { it in WHITELIST_WORK }.toSet()
            "family" -> allBlockedPackages // Hammasi (faqat qo'ng'iroq oldin)
            "sleep" -> allBlockedPackages // Hammasi
            else -> allBlockedPackages
        }
    }

    data class Categorization(val category: String, val severity: String)

    private data class Rule(
        val keywords: List<String>,
        val categorization: Categorization
    ) {
        fun matches(text: String): Boolean = keywords.any { text.contains(it) }
    }

    companion object {
        // Sodir bo'lgan tartib muhim — yuqorigi qoidalar avval tekshiriladi
        private val RULES = listOf(
            Rule(
                keywords = listOf("qur'on", "quran", "namoz", "salat", "ibodat", "tahajjud", "duo"),
                categorization = Categorization("worship", ScheduledSessionEntity.SEVERITY_MAX)
            ),
            Rule(
                keywords = listOf("uxlash", "uyqu", "sleep", "yotish"),
                categorization = Categorization("sleep", ScheduledSessionEntity.SEVERITY_HIGH)
            ),
            Rule(
                keywords = listOf("yugurish", "sport", "gym", "fitness", "mashq", "trening"),
                categorization = Categorization("physical", ScheduledSessionEntity.SEVERITY_MEDIUM)
            ),
            Rule(
                keywords = listOf("imtihon", "dars", "darslik", "kitob", "study", "test"),
                categorization = Categorization("study", ScheduledSessionEntity.SEVERITY_HIGH)
            ),
            Rule(
                keywords = listOf("ish", "kod", "migratsiya", "pull request", "meeting", "uchrashuv"),
                categorization = Categorization("work", ScheduledSessionEntity.SEVERITY_HIGH)
            ),
            Rule(
                keywords = listOf("oilaviy", "oila", "ovqat", "tushlik", "kechki ovqat"),
                categorization = Categorization("family", ScheduledSessionEntity.SEVERITY_MEDIUM)
            )
        )

        // Kategoriya bo'yicha "ruxsat etilgan" ilovalar
        private val WHITELIST_STUDY = setOf(
            "com.spotify.music",
            "com.google.android.apps.docs",
            "com.notion.id"
        )

        private val WHITELIST_PHYSICAL = setOf(
            "com.spotify.music",
            "com.google.android.youtube.music"
        )

        private val WHITELIST_WORK = setOf(
            "com.slack",
            "com.microsoft.teams",
            "com.google.android.gm",  // Gmail
            "com.tdesktop.android"     // Telegram desktop chats
        )
    }
}
