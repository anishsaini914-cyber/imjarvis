package com.jarvis.assistant.data.remote.api

import com.jarvis.assistant.data.remote.dto.AgentRouterRequest
import com.jarvis.assistant.data.remote.dto.AgentRouterResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AgentRouterApi {
    @POST("v1/chat/completions")
    suspend fun chatCompletion(@Header("Authorization") authorization: String, @Body request: AgentRouterRequest): AgentRouterResponse
}
