package com.jarvis.assistant.data.repository

import com.jarvis.assistant.data.remote.AgentRouterApi
import com.jarvis.assistant.data.remote.GeminiApi
import com.jarvis.assistant.data.remote.OpenAiApi
import com.jarvis.assistant.data.remote.dto.GeminiConfig
import com.jarvis.assistant.data.remote.dto.GeminiContent
import com.jarvis.assistant.data.remote.dto.GeminiPart
import com.jarvis.assistant.data.remote.dto.GeminiRequest
import com.jarvis.assistant.data.remote.dto.OpenAiChatRequest
import com.jarvis.assistant.data.remote.dto.OpenAiMessage
import com.jarvis.assistant.data.security.SecureStorage
import com.jarvis.assistant.domain.model.Message
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AiRepository @Inject constructor(
    private val openAiApi: OpenAiApi,
    private val geminiApi: GeminiApi,
    private val agentRouterApi: AgentRouterApi,
    private val secureStorage: SecureStorage
) {
    suspend fun getChatCompletion(messages: List<Message>, provider: String, model: String): Result<String> = when (provider) {
        "openai" -> getOpenAiCompletion(messages, model)
        "gemini" -> getGeminiCompletion(messages, model)
        "agentrouter" -> getAgentRouterCompletion(messages, model)
        else -> Result.failure(Exception("Unknown provider: $provider"))
    }

    private suspend fun getOpenAiCompletion(messages: List<Message>, model: String): Result<String> {
        val key = secureStorage.getString(SecureStorage.KEY_OPENAI_KEY)
        if (key.isBlank()) return Result.failure(Exception("OpenAI API key not configured"))
        return try {
            val resp = openAiApi.chatCompletion("Bearer $key", OpenAiChatRequest(model = model, messages = messages.map { OpenAiMessage(role = it.role, content = it.content) }))
            if (resp.isSuccessful) Result.success(resp.body()?.choices?.firstOrNull()?.message?.content ?: "No response")
            else Result.failure(Exception("OpenAI error: ${resp.code()}"))
        } catch (e: Exception) { Result.failure(e) }
    }

    private suspend fun getGeminiCompletion(messages: List<Message>, model: String): Result<String> {
        val key = secureStorage.getString(SecureStorage.KEY_GEMINI_KEY)
        if (key.isBlank()) return Result.failure(Exception("Gemini API key not configured"))
        return try {
            val gm = if (model.isBlank()) "gemini-2.0-flash" else model
            val resp = geminiApi.generateContent(gm, key, GeminiRequest(contents = messages.map {
                GeminiContent(role = if (it.role == "assistant") "model" else "user", parts = listOf(GeminiPart(text = it.content)))
            }, config = GeminiConfig()))
            if (resp.isSuccessful) Result.success(resp.body()?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "No response")
            else Result.failure(Exception("Gemini error: ${resp.code()}"))
        } catch (e: Exception) { Result.failure(e) }
    }

    private suspend fun getAgentRouterCompletion(messages: List<Message>, model: String): Result<String> {
        val key = secureStorage.getString(SecureStorage.KEY_AGENT_ROUTER_KEY)
        if (key.isBlank()) return Result.failure(Exception("AgentRouter API key not configured"))
        return try {
            val resp = agentRouterApi.chatCompletion("Bearer $key", OpenAiChatRequest(model = model, messages = messages.map { OpenAiMessage(role = it.role, content = it.content) }))
            if (resp.isSuccessful) Result.success(resp.body()?.choices?.firstOrNull()?.message?.content ?: "No response")
            else Result.failure(Exception("AgentRouter error: ${resp.code()}"))
        } catch (e: Exception) { Result.failure(e) }
    }
}
