package com.jarvis.assistant.data.api

import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApi {
    @POST("v1beta/models/{model}:generateContent")
    suspend fun generateContent(
        @Path("model", encoded = true) model: String,
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

data class GeminiRequest(
    val contents: List<GeminiContent>,
    val generationConfig: GenerationConfig? = null
)

data class GeminiContent(val parts: List<GeminiPart>, val role: String = "user")
data class GeminiPart(val text: String)
data class GenerationConfig(val temperature: Double = 0.7, val maxOutputTokens: Int = 2048)
data class GeminiResponse(val candidates: List<GeminiCandidate>?)
data class GeminiCandidate(val content: GeminiContent?, val finishReason: String?)
