package com.jarvis.assistant.ai

import com.jarvis.assistant.domain.model.ChatMessage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiManager @Inject constructor(
    private val openAi: OpenAiClient,
    private val gemini: GeminiClient,
    private val agentRouter: AgentRouterClient
) {
    suspend fun sendMessage(messages: List<ChatMessage>, provider: String, model: String = ""): Result<String> = when (provider) {
        "openai" -> openAi.sendMessage(messages, model)
        "gemini" -> gemini.sendMessage(messages, model)
        "agentrouter" -> agentRouter.sendMessage(messages, model)
        else -> openAi.sendMessage(messages, model)
    }
}
