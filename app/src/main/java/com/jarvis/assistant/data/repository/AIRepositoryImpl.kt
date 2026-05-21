package com.jarvis.assistant.data.repository

import com.jarvis.assistant.data.api.AgentRouterApi
import com.jarvis.assistant.data.api.AgentRouterMessage
import com.jarvis.assistant.data.api.AgentRouterRequest
import com.jarvis.assistant.data.api.GeminiApi
import com.jarvis.assistant.data.api.GeminiContent
import com.jarvis.assistant.data.api.GeminiPart
import com.jarvis.assistant.data.api.GeminiRequest
import com.jarvis.assistant.data.api.Message as OpenAIMessage
import com.jarvis.assistant.data.api.OpenAIRequest
import com.jarvis.assistant.data.api.OpenAiApi
import com.jarvis.assistant.data.storage.SettingsStorage
import com.jarvis.assistant.domain.model.AIProvider
import com.jarvis.assistant.domain.repository.AIRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AIRepositoryImpl @Inject constructor(
    private val openAiApi: OpenAiApi,
    private val geminiApi: GeminiApi,
    private val agentRouterApi: AgentRouterApi,
    private val settingsStorage: SettingsStorage
) : AIRepository {

    override suspend fun sendMessage(message: String, provider: AIProvider, model: String): Result<String> {
        return try {
            when (provider) {
                AIProvider.OPENAI -> sendToOpenAI(message, model)
                AIProvider.GEMINI -> sendToGemini(message, model)
                AIProvider.AGENTROUTER -> sendToAgentRouter(message, model)
                AIProvider.LOCAL -> Result.failure(UnsupportedOperationException("Local model not yet implemented"))
            }
        } catch (e: Exception) { Result.failure(e) }
    }

    override suspend fun sendMessageStream(message: String, provider: AIProvider, model: String): Result<Flow<String>> {
        return try {
            Result.success(flow {
                sendMessage(message, provider, model).onSuccess { emit(it) }
            })
        } catch (e: Exception) { Result.failure(e) }
    }

    private suspend fun sendToOpenAI(message: String, model: String): Result<String> {
        val apiKey = settingsStorage.getOpenAIApiKeySync()
        if (apiKey.isBlank()) return Result.failure(Exception("OpenAI API key not configured"))
        val response = openAiApi.chatCompletion(
            authorization = "Bearer $apiKey",
            request = OpenAIRequest(
                model = model.ifBlank { settingsStorage.getOpenAIModelSync() },
                messages = listOf(OpenAIMessage("system", SYSTEM_PROMPT), OpenAIMessage("user", message))
            )
        )
        val content = response.choices.firstOrNull()?.message?.content
            ?: return Result.failure(Exception("No response from OpenAI"))
        return Result.success(content)
    }

    private suspend fun sendToGemini(message: String, model: String): Result<String> {
        val apiKey = settingsStorage.getGeminiApiKeySync()
        if (apiKey.isBlank()) return Result.failure(Exception("Gemini API key not configured"))
        val response = geminiApi.generateContent(
            model = "models/${model.ifBlank { settingsStorage.getGeminiModelSync() }}",
            apiKey = apiKey,
            request = GeminiRequest(contents = listOf(GeminiContent(parts = listOf(GeminiPart(text = message)))))
        )
        val content = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
            ?: return Result.failure(Exception("No response from Gemini"))
        return Result.success(content)
    }

    private suspend fun sendToAgentRouter(message: String, model: String): Result<String> {
        val apiKey = settingsStorage.getAgentRouterApiKeySync()
        if (apiKey.isBlank()) return Result.failure(Exception("AgentRouter API key not configured"))
        val response = agentRouterApi.chatCompletion(
            authorization = "Bearer $apiKey",
            request = AgentRouterRequest(
                model = model.ifBlank { settingsStorage.getAgentRouterModelSync() },
                messages = listOf(AgentRouterMessage("system", SYSTEM_PROMPT), AgentRouterMessage("user", message))
            )
        )
        if (response.error != null) return Result.failure(Exception(response.error.message ?: "AgentRouter error"))
        val content = response.choices?.firstOrNull()?.message?.content
            ?: return Result.failure(Exception("No response from AgentRouter"))
        return Result.success(content)
    }

    companion object {
        private const val SYSTEM_PROMPT = "You are JARVIS, an advanced AI assistant. You are helpful, concise, and capable. Keep responses brief and actionable."
    }
}
