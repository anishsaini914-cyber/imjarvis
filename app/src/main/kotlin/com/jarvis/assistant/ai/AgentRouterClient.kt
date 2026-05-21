package com.jarvis.assistant.ai

import com.jarvis.assistant.data.local.preferences.EncryptedPreferences
import com.jarvis.assistant.data.remote.AiApiService
import com.jarvis.assistant.data.remote.ChatMsg
import com.jarvis.assistant.data.remote.ChatRequest
import com.jarvis.assistant.domain.model.ChatMessage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AgentRouterClient @Inject constructor(
    private val api: AiApiService,
    private val prefs: EncryptedPreferences
) {
    suspend fun sendMessage(messages: List<ChatMessage>, model: String = ""): Result<String> {
        val key = prefs.agentRouterApiKey
        if (key.isBlank()) return Result.failure(Exception("AgentRouter key not configured"))
        return try {
            val ep = prefs.agentRouterEndpoint
            val res = api.sendChatMessage("$ep/chat/completions", "Bearer $key",
                ChatRequest(model = model.ifBlank { prefs.agentRouterModel }, messages = messages.map { ChatMsg(it.role, it.content) }))
            if (res.isSuccessful) Result.success(res.body()?.choices?.firstOrNull()?.message?.content ?: "")
            else Result.failure(Exception("AgentRouter error: ${res.code()}"))
        } catch (e: Exception) { Result.failure(e) }
    }
}
