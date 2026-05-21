package com.jarvis.assistant.data.remote.dto

import com.google.gson.annotations.SerializedName

data class OpenAiChatRequest(
    val model: String = "gpt-4o-mini",
    val messages: List<OpenAiMessage>,
    val temperature: Double = 0.7,
    @SerializedName("max_tokens") val maxTokens: Int = 1024,
    val stream: Boolean = false
)
data class OpenAiMessage(val role: String, val content: String)
data class OpenAiChatResponse(val id: String? = null, val choices: List<OpenAiChoice>? = null, val error: OpenAiError? = null)
data class OpenAiChoice(val index: Int = 0, val message: OpenAiMessage? = null, @SerializedName("finish_reason") val finishReason: String? = null)
data class OpenAiError(val message: String? = null, val type: String? = null, val code: String? = null)
data class OpenAiModelResponse(val data: List<OpenAiModelInfo>? = null)
data class OpenAiModelInfo(val id: String? = null, val ownedBy: String? = null)
