package com.jarvis.assistant.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GeminiRequest(val contents: List<GeminiContent>? = null, @SerializedName("generationConfig") val config: GeminiConfig? = null)
data class GeminiContent(val role: String = "user", val parts: List<GeminiPart>? = null)
data class GeminiPart(val text: String? = null)
data class GeminiConfig(val temperature: Double = 0.7, @SerializedName("maxOutputTokens") val maxTokens: Int = 1024)
data class GeminiResponse(val candidates: List<GeminiCandidate>? = null, val error: GeminiError? = null)
data class GeminiCandidate(val content: GeminiContent? = null, @SerializedName("finishReason") val finishReason: String? = null)
data class GeminiError(val message: String? = null, val code: Int? = null)
