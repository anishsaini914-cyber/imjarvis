package com.jarvis.assistant.data.api

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAiApi {
    @POST("v1/chat/completions")
    suspend fun chatCompletion(
        @Header("Authorization") authorization: String,
        @Body request: OpenAIRequest
    ): OpenAIResponse
}

data class OpenAIRequest(
    val model: String,
    val messages: List<Message>,
    val temperature: Double = 0.7,
    val max_tokens: Int = 2048,
    val stream: Boolean = false
)

data class Message(val role: String, val content: String)

data class OpenAIResponse(
    val id: String, val `object`: String, val created: Long, val model: String,
    val choices: List<Choice>, val usage: Usage?
)

data class Choice(val index: Int, val message: Message, val finish_reason: String?)
data class Usage(val prompt_tokens: Int, val completion_tokens: Int, val total_tokens: Int)
