package com.jarvis.assistant.data.remote.api

import com.jarvis.assistant.data.remote.dto.GeminiChatRequest
import com.jarvis.assistant.data.remote.dto.GeminiChatResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface GeminiApi {
    @POST("v1beta/models/gemini-pro:generateContent")
    suspend fun generateContent(@Query("key") apiKey: String, @Body request: GeminiChatRequest): GeminiChatResponse
}
