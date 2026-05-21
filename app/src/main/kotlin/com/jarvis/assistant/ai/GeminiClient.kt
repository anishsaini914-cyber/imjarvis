package com.jarvis.assistant.ai

import com.jarvis.assistant.data.local.preferences.EncryptedPreferences
import com.jarvis.assistant.data.remote.AiApiService
import com.jarvis.assistant.data.remote.ChatMsg
import com.jarvis.assistant.data.remote.ChatRequest
import com.jarvis.assistant.domain.model.ChatMessage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiClient @Inject constructor(
    private val api: AiApiService,
    private val prefs: EncryptedPreferences
) {
    suspend fun sendMessage(messages: List<ChatMessage>, model: String = ""): Result<String> {
        val key = prefs.geminiApiKey
        if (key.isBlank()) return Result.failure(Exception("Gemini API key not configured"))
        return try {
            val m = model.ifBlank { prefs.geminiModel }
            val res = api.sendChatMessage("https://generativelanguage.googleapis.com/v1beta/models/$m:generateContent?key=$key", "",
                ChatRequest(model = "", messages = messages.map { ChatMsg(it.role, it.content) }))
            if (res.isSuccessful) Result.success(res.body()?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "")
            else Result.failure(Exception("Gemini error: ${res.code()}"))
        } catch (e: Exception) { Result.failure(e) }
    }
}
