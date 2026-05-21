package com.jarvis.assistant.data.api

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AgentRouterApi {
    @POST("v1/chat/completions")
    suspend fun chatCompletion(
        @Header("Authorization") authorization: String,
        @Body request: AgentRouterRequest
    ): AgentRouterResponse
}

data class AgentRouterRequest(
    val model: String, val messages: List<AgentRouterMessage>,
    val temperature: Double = 0.7, val max_tokens: Int = 2048
)

data class AgentRouterMessage(val role: String, val content: String)

data class AgentRouterResponse(
    val id: String?, val choices: List<AgentRouterChoice>?, val error: AgentRouterError?
)

data class AgentRouterChoice(val message: AgentRouterMessage?, val finish_reason: String?)
data class AgentRouterError(val message: String?, val type: String?)
