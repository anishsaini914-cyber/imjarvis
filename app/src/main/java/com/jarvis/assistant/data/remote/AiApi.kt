package com.jarvis.assistant.data.remote

import com.jarvis.assistant.data.remote.dto.GeminiRequest
import com.jarvis.assistant.data.remote.dto.GeminiResponse
import com.jarvis.assistant.data.remote.dto.OpenAiChatRequest
import com.jarvis.assistant.data.remote.dto.OpenAiChatResponse
import com.jarvis.assistant.data.remote.dto.OpenAiModelResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenAiApi {
    @POST("v1/chat/completions")
    suspend fun chatCompletion(@Header("Authorization") auth: String, @Body request: OpenAiChatRequest): Response<OpenAiChatResponse>

    @GET("v1/models")
    suspend fun listModels(@Header("Authorization") auth: String): Response<OpenAiModelResponse>
}

interface GeminiApi {
    @POST("v1beta/models/{model}:generateContent")
    suspend fun generateContent(@Path("model") model: String, @Query("key") apiKey: String, @Body request: GeminiRequest): Response<GeminiResponse>
}

interface AgentRouterApi {
    @POST("api/chat/completions")
    suspend fun chatCompletion(@Header("Authorization") auth: String, @Body request: OpenAiChatRequest): Response<OpenAiChatResponse>

    @GET("api/models")
    suspend fun listModels(@Header("Authorization") auth: String): Response<OpenAiModelResponse>
}
