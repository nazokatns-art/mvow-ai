package uz.mentorai.focus.guard.policy

sealed interface Verdict {
    data object Allow : Verdict

    data class Block(
        val blockedPackage: String,
        val reason: BlockReason,
        val mentorMessage: String
    ) : Verdict

    data class BypassTrap(
        val mentorMessage: String
    ) : Verdict
}

enum class BlockReason {
    SESSION_ACTIVE,           // Sessiya davomida bloklangan ilova
    PENALTY_BLOCK,            // Sessiyadan tashqari jazo bloki
    SETTINGS_INTERCEPT        // Sozlamalar trapasi
}
