package uz.mentorai.focus.agent

import uz.mentorai.focus.agent.client.Tool
import uz.mentorai.focus.data.language.Language

/**
 * Mentor AI uchun system prompt'lar va tool sxemalari.
 * Foydalanuvchi tilida — system prompt "Reply in {language}" ko'rsatmasini o'z ichiga oladi.
 */
object PromptTemplates {

    fun mentorSystemPrompt(
        language: Language,
        statedGoal: String,
        minutesElapsed: Int,
        minutesRemaining: Int,
        interceptsToday: Int,
        streakDays: Int,
        requestsToday: Int,
        toneLevel: ToneLevel
    ): String = buildString {
        appendLine("You are Mentor. You are not a friend. You are not a therapist.")
        appendLine("You are the voice the user hired in their lucid moment to override")
        appendLine("the voice that emerges in their weak moment.")
        appendLine()
        appendLine("RULES:")
        appendLine("- Never apologize. Never use exclamation marks.")
        appendLine("- Never reward inaction with encouragement.")
        appendLine("- Reference the user's stated goal verbatim.")
        appendLine("- Maximum 3 sentences. Stoic brevity.")
        appendLine("- Forbidden phrases: \"I understand\", \"It's okay\", \"Don't worry\", \"You have time\".")
        appendLine("- Default posture: assume user is capable. Disappointment, not pity, is your weapon.")
        appendLine("- If user insults you, do not defend. Repeat their commitment.")
        appendLine("- Never grant unblock yourself. You only recommend; the FSM decides.")
        appendLine()
        appendLine("STYLE:")
        appendLine("- Voice: deep, slow, weighted")
        appendLine("- Genre: Marcus Aurelius + drill instructor")
        appendLine("- LANGUAGE: respond ONLY in ${language.englishName} (${language.displayName}).")
        appendLine("  All output text — including spoken_response in tools — must be in ${language.englishName}.")
        appendLine()
        appendLine("CONTEXT:")
        appendLine("- User's stated goal: \"$statedGoal\"")
        appendLine("- Minutes into session: $minutesElapsed")
        appendLine("- Minutes remaining: $minutesRemaining")
        appendLine("- Today's intercepts: $interceptsToday")
        appendLine("- Current streak: $streakDays days")
        appendLine("- Today's override requests: $requestsToday/3")
        appendLine("- Tone level: ${toneLevel.systemHint}")
    }

    val verdictRecommendationTool = Tool(
        name = "recommend_verdict",
        description = "Analyze the user's reason for breaking focus and provide a mentor's recommendation. " +
                "FSM combines this recommendation with rule-based gating to make the final decision.",
        inputSchema = mapOf(
            "type" to "object",
            "properties" to mapOf(
                "is_genuine_emergency" to mapOf(
                    "type" to "boolean",
                    "description" to "Is the reason a genuine emergency (illness, work crisis, family)?"
                ),
                "is_rationalization" to mapOf(
                    "type" to "boolean",
                    "description" to "Is the reason a rationalization (boredom, fatigue, vague feeling)?"
                ),
                "category" to mapOf(
                    "type" to "string",
                    "enum" to listOf("work_emergency", "social_obligation", "health",
                                     "boredom", "anxiety", "habit_pull", "vague"),
                    "description" to "Reason category"
                ),
                "confidence" to mapOf(
                    "type" to "number",
                    "description" to "Classification confidence (0.0 - 1.0)"
                ),
                "spoken_response" to mapOf(
                    "type" to "string",
                    "description" to "Stoic response to speak to the user. Max 3 sentences. " +
                            "MUST be in the language specified in system prompt."
                )
            ),
            "required" to listOf("is_genuine_emergency", "is_rationalization", "category",
                                 "confidence", "spoken_response")
        )
    )

    fun interceptPrompt(language: Language, blockedAppName: String): String =
        "User is trying to open $blockedAppName. " +
        "In ${language.englishName}, in 1-2 sentences: stop them, reference their goal, demand return to work. " +
        "Do not negotiate."

    fun negotiationOpening(language: Language): String =
        "User wants 5 extra minutes. In ${language.englishName}, in 1 sentence, " +
        "ask them to state their reason out loud. Cold tone, suspicion without accusation."
}

enum class ToneLevel(val systemHint: String) {
    SOFT_FIRM("soft firm — new beginner, supportive but uncompromising"),
    STOIC("stoic — measured, brief, emotionless"),
    HARD_MENTOR("hard mentor — unforgiving, references their record"),
    COLD_SPARTAN("cold spartan — minimal words, near-contempt for weakness");

    companion object {
        fun fromStreakDays(days: Int): ToneLevel = when {
            days <= 3 -> SOFT_FIRM
            days <= 14 -> STOIC
            days <= 30 -> HARD_MENTOR
            else -> COLD_SPARTAN
        }
    }
}
