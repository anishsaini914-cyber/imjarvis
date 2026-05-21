package com.jarvis.assistant.data.repository

import com.jarvis.assistant.data.storage.SettingsStorage
import com.jarvis.assistant.domain.model.AIProvider
import com.jarvis.assistant.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val settingsStorage: SettingsStorage
) : SettingsRepository {
    override fun getActiveProvider(): Flow<AIProvider> = settingsStorage.getActiveProvider()
    override suspend fun setActiveProvider(p: AIProvider) = settingsStorage.setActiveProvider(p)
    override fun getOpenAIApiKey(): Flow<String> = settingsStorage.getOpenAIApiKey()
    override suspend fun setOpenAIApiKey(k: String) = settingsStorage.setOpenAIApiKey(k)
    override fun getOpenAIModel(): Flow<String> = settingsStorage.getOpenAIModel()
    override suspend fun setOpenAIModel(m: String) = settingsStorage.setOpenAIModel(m)
    override fun getOpenAIEndpoint(): Flow<String> = settingsStorage.getOpenAIEndpoint()
    override suspend fun setOpenAIEndpoint(e: String) = settingsStorage.setOpenAIEndpoint(e)
    override fun getGeminiApiKey(): Flow<String> = settingsStorage.getGeminiApiKey()
    override suspend fun setGeminiApiKey(k: String) = settingsStorage.setGeminiApiKey(k)
    override fun getGeminiModel(): Flow<String> = settingsStorage.getGeminiModel()
    override suspend fun setGeminiModel(m: String) = settingsStorage.setGeminiModel(m)
    override fun getAgentRouterApiKey(): Flow<String> = settingsStorage.getAgentRouterApiKey()
    override suspend fun setAgentRouterApiKey(k: String) = settingsStorage.setAgentRouterApiKey(k)
    override fun getAgentRouterEndpoint(): Flow<String> = settingsStorage.getAgentRouterEndpoint()
    override suspend fun setAgentRouterEndpoint(e: String) = settingsStorage.setAgentRouterEndpoint(e)
    override fun getAgentRouterModel(): Flow<String> = settingsStorage.getAgentRouterModel()
    override suspend fun setAgentRouterModel(m: String) = settingsStorage.setAgentRouterModel(m)
    override fun isWakeWordEnabled(): Flow<Boolean> = settingsStorage.isWakeWordEnabled()
    override suspend fun setWakeWordEnabled(e: Boolean) = settingsStorage.setWakeWordEnabled(e)
    override fun getWakeWordPhrase(): Flow<String> = settingsStorage.getWakeWordPhrase()
    override suspend fun setWakeWordPhrase(p: String) = settingsStorage.setWakeWordPhrase(p)
    override fun getWakeWordSensitivity(): Flow<Int> = settingsStorage.getWakeWordSensitivity()
    override suspend fun setWakeWordSensitivity(s: Int) = settingsStorage.setWakeWordSensitivity(s)
    override fun isOverlayEnabled(): Flow<Boolean> = settingsStorage.isOverlayEnabled()
    override suspend fun setOverlayEnabled(e: Boolean) = settingsStorage.setOverlayEnabled(e)
    override fun getTheme(): Flow<String> = settingsStorage.getTheme()
    override suspend fun setTheme(t: String) = settingsStorage.setTheme(t)
    override fun getLanguage(): Flow<String> = settingsStorage.getLanguage()
    override suspend fun setLanguage(l: String) = settingsStorage.setLanguage(l)
    override fun isOnboardingCompleted(): Flow<Boolean> = settingsStorage.isOnboardingCompleted()
    override suspend fun setOnboardingCompleted(c: Boolean) = settingsStorage.setOnboardingCompleted(c)
}
