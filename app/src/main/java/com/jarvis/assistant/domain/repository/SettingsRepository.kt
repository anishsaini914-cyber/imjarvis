package com.jarvis.assistant.domain.repository

import com.jarvis.assistant.domain.model.AIProvider
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getActiveProvider(): Flow<AIProvider>
    suspend fun setActiveProvider(provider: AIProvider)
    fun getOpenAIApiKey(): Flow<String>
    suspend fun setOpenAIApiKey(key: String)
    fun getOpenAIModel(): Flow<String>
    suspend fun setOpenAIModel(model: String)
    fun getOpenAIEndpoint(): Flow<String>
    suspend fun setOpenAIEndpoint(endpoint: String)
    fun getGeminiApiKey(): Flow<String>
    suspend fun setGeminiApiKey(key: String)
    fun getGeminiModel(): Flow<String>
    suspend fun setGeminiModel(model: String)
    fun getAgentRouterApiKey(): Flow<String>
    suspend fun setAgentRouterApiKey(key: String)
    fun getAgentRouterEndpoint(): Flow<String>
    suspend fun setAgentRouterEndpoint(endpoint: String)
    fun getAgentRouterModel(): Flow<String>
    suspend fun setAgentRouterModel(model: String)
    fun isWakeWordEnabled(): Flow<Boolean>
    suspend fun setWakeWordEnabled(enabled: Boolean)
    fun getWakeWordPhrase(): Flow<String>
    suspend fun setWakeWordPhrase(phrase: String)
    fun getWakeWordSensitivity(): Flow<Int>
    suspend fun setWakeWordSensitivity(sensitivity: Int)
    fun isOverlayEnabled(): Flow<Boolean>
    suspend fun setOverlayEnabled(enabled: Boolean)
    fun getTheme(): Flow<String>
    suspend fun setTheme(theme: String)
    fun getLanguage(): Flow<String>
    suspend fun setLanguage(language: String)
    fun isOnboardingCompleted(): Flow<Boolean>
    suspend fun setOnboardingCompleted(completed: Boolean)
}
