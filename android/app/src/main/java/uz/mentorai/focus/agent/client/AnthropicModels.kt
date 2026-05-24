package uz.mentorai.focus.agent.client

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Anthropic Messages API request/response modellari.
 * Streaming SSE event'lari `StreamEvent` orqali tarqatiladi.
 */

@JsonClass(generateAdapter = true)
data class MessagesRequest(
    val model: String,
    @Json(name = "max_tokens") val maxTokens: Int,
    val messages: List<Message>,
    val system: String? = null,
    val stream: Boolean = true,
    val temperature: Double = 0.7,
    val tools: List<Tool>? = null,
    @Json(name = "tool_choice") val toolChoice: ToolChoice? = null
)

@JsonClass(generateAdapter = true)
data class Message(
    val role: String,            // "user" | "assistant"
    val content: String
)

@JsonClass(generateAdapter = true)
data class Tool(
    val name: String,
    val description: String,
    @Json(name = "input_schema") val inputSchema: Map<String, Any>
)

@JsonClass(generateAdapter = true)
data class ToolChoice(
    val type: String,            // "tool" | "auto" | "any"
    val name: String? = null
)

/** Streaming chiqishni soddalashtirilgan event'larga ajratamiz */
sealed interface StreamEvent {
    data class TextDelta(val text: String) : StreamEvent
    data class ToolUseStart(val toolName: String, val toolUseId: String) : StreamEvent
    data class ToolUseInputJson(val partialJson: String) : StreamEvent
    data class MessageStop(val stopReason: String?) : StreamEvent
    data class Error(val message: String) : StreamEvent
}

object Models {
    const val HAIKU_4_5 = "claude-haiku-4-5-20251001"
    const val SONNET_4_6 = "claude-sonnet-4-6"
}
