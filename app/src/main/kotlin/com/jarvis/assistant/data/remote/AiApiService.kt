package com.jarvis.assistant.data.remote

import retrofit2.Response
import retrofit2.http.*

interface AiApiService {
    @POST
    suspend fun sendChatMessage(@Url url: String, @Header("Authorization") auth: String, @Body request: ChatRequest): Response<ChatResponse>
}

data class ChatRequest(val model: String, val messages: List<ChatMsg>, val temperature: Double = 0.7, val max_tokens: Int = 2048)
data class ChatMsg(val role: String, val content: String)
data class ChatResponse(val id: String? = null, val choices: List<Choice>? = null, val candidates: List<Candidate>? = null)
data class Choice(val message: MsgContent? = null)
data class MsgContent(val role: String? = null, val content: String? = null)
data class Candidate(val content: CandContent? = null)
data class CandContent(val parts: List<Part>? = null)
data class Part(val text: String? = null)
