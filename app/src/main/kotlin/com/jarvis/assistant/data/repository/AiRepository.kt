package com.jarvis.assistant.data.repository

import com.jarvis.assistant.ai.AiManager
import com.jarvis.assistant.domain.model.ChatMessage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiRepository @Inject constructor(private val aiManager: AiManager) {
    suspend fun sendMessage(messages: List<ChatMessage>, provider: String, model: String = ""): Result<String> =
        aiManager.sendMessage(messages, provider, model)
}
