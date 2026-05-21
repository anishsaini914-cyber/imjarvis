package com.jarvis.assistant.data.remote.api

import com.jarvis.assistant.data.remote.dto.OpenAiChatRequest
import com.jarvis.assistant.data.remote.dto.OpenAiChatResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAiApi {
    @POST("v1/chat/completions")
    suspend fun chatCompletion(@Header("Authorization") authorization: String, @Body request: OpenAiChatRequest): OpenAiChatResponse
}
