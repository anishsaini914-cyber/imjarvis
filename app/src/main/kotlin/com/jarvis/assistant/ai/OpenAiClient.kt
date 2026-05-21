package com.jarvis.assistant.ai

import com.jarvis.assistant.data.local.preferences.EncryptedPreferences
import com.jarvis.assistant.data.remote.AiApiService
import com.jarvis.assistant.data.remote.ChatMsg
import com.jarvis.assistant.data.remote.ChatRequest
import com.jarvis.assistant.domain.model.ChatMessage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OpenAiClient @Inject constructor(
    private val api: AiApiService,
    private val prefs: EncryptedPreferences
) {
    suspend fun sendMessage(messages: List<ChatMessage>, model: String = ""): Result<String> {
        val key = prefs.openAiApiKey
        if (key.isBlank()) return Result.failure(Exception("OpenAI API key not configured"))
        return try {
            val res = api.sendChatMessage("https://api.openai.com/v1/chat/completions", "Bearer $key",
                ChatRequest(model = model.ifBlank { prefs.openAiModel }, messages = messages.map { ChatMsg(it.role, it.content) }))
            if (res.isSuccessful) Result.success(res.body()?.choices?.firstOrNull()?.message?.content ?: "")
            else Result.failure(Exception("OpenAI error: ${res.code()}"))
        } catch (e: Exception) { Result.failure(e) }
    }
}
