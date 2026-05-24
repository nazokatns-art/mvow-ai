package uz.mentorai.focus.guard.policy

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import uz.mentorai.focus.data.language.LanguageRepository
import uz.mentorai.focus.data.session.SessionEntity
import uz.mentorai.focus.i18n.MentorPhrases
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Asosiy qoidalar dvigateli — package ochilganda ruxsatmi yoki to'siqmi qaror qiladi.
 *
 * MVP — Kotlin in-memory. Sprint 4'da temporary grant qo'shildi.
 * V2'da Rust core'ga ko'chiriladi (anti-tamper uchun, server-signed token bilan).
 */
@Singleton
class PolicyEngine @Inject constructor(
    private val languageRepository: LanguageRepository
) {

    private val _activeSession = MutableStateFlow<SessionEntity?>(null)
    val activeSession: StateFlow<SessionEntity?> = _activeSession.asStateFlow()

    /** Vaqtinchalik ruxsat berilgan paketlar: pkg → untilMs */
    private val _temporaryGrants = MutableStateFlow<Map<String, Long>>(emptyMap())
    val temporaryGrants: StateFlow<Map<String, Long>> = _temporaryGrants.asStateFlow()

    val blockedPackages: Set<String>
        get() = _activeSession.value?.blockedPackages.orEmpty()

    fun setActiveSession(session: SessionEntity?) {
        _activeSession.value = session
        if (session == null) {
            // Sessiya tugadi — barcha temporary grant'larni tozalash
            _temporaryGrants.value = emptyMap()
        }
    }

    fun isInActiveSession(): Boolean = _activeSession.value != null

    /** Negotiation FSM `Grant` qarorida chaqiriladi */
    fun applyTemporaryGrant(packages: Set<String>, durationMillis: Long) {
        val until = System.currentTimeMillis() + durationMillis
        _temporaryGrants.value = _temporaryGrants.value + packages.associateWith { until }
    }

    fun revokeAllTemporaryGrants() {
        _temporaryGrants.value = emptyMap()
    }

    /** Muddati o'tgan grant'larni tozalash — har evaluate'da chaqiriladi */
    private fun pruneExpiredGrants() {
        val now = System.currentTimeMillis()
        val current = _temporaryGrants.value
        val pruned = current.filterValues { it > now }
        if (pruned.size != current.size) {
            _temporaryGrants.value = pruned
        }
    }

    fun evaluate(packageName: String, ownPackageName: String): Verdict {
        pruneExpiredGrants()
        val session = _activeSession.value ?: return Verdict.Allow

        // 1. O'z ilovamiz — har doim ruxsat
        if (packageName == ownPackageName) return Verdict.Allow

        // 2. Settings ekranida bypass-trap evaluateSettingsScreen orqali tekshiriladi
        if (packageName == ANDROID_SETTINGS_PKG) return Verdict.Allow

        // 3. Vaqtinchalik ruxsat
        if (_temporaryGrants.value[packageName] != null) {
            return Verdict.Allow
        }

        // 4. Bloklangan paket
        if (packageName in session.blockedPackages) {
            return Verdict.Block(
                blockedPackage = packageName,
                reason = BlockReason.SESSION_ACTIVE,
                mentorMessage = mentorMessageFor(session, packageName)
            )
        }

        return Verdict.Allow
    }

    fun evaluateSettingsScreen(screenLabel: String?): Verdict {
        if (!isInActiveSession()) return Verdict.Allow
        val label = screenLabel?.lowercase() ?: return Verdict.Allow

        val isMentorSettings = TRAP_KEYWORDS.any { label.contains(it) }

        if (isMentorSettings) {
            return Verdict.BypassTrap(
                mentorMessage = MentorPhrases.bypassTrap(languageRepository.current.value)
            )
        }
        return Verdict.Allow
    }

    private fun mentorMessageFor(session: SessionEntity, blockedPkg: String): String {
        val now = System.currentTimeMillis()
        val remainingMs = (session.plannedEndAtMillis - now).coerceAtLeast(0L)
        val remainingMin = remainingMs / 60_000L

        return MentorPhrases.interceptStop(
            language = languageRepository.current.value,
            goal = session.goal,
            remainingMinutes = remainingMin
        )
    }

    companion object {
        const val ANDROID_SETTINGS_PKG = "com.android.settings"

        // Settings ekranida bypass-trap'ni ushlash uchun kalit so'zlar (ko'p tilli)
        private val TRAP_KEYWORDS = listOf(
            "mentor",                      // ilova nomi
            "accessibility",               // EN
            "maxsus imkoniyatlar",         // UZ
            "специальные возможности",     // RU
            "accesibilidad",               // ES
            "إمكانية الوصول",                // AR
            "erişilebilirlik"              // TR
        )
    }
}
