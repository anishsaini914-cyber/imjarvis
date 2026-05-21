package com.jarvis.assistant.data.repository

import com.jarvis.assistant.data.security.SecureStorage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(private val ss: SecureStorage) {
    fun getActiveProvider(): String = ss.getString(SecureStorage.KEY_ACTIVE_PROVIDER, "openai")
    fun setActiveProvider(p: String) = ss.saveString(SecureStorage.KEY_ACTIVE_PROVIDER, p)
    fun getOpenAiKey(): String = ss.getString(SecureStorage.KEY_OPENAI_KEY)
    fun setOpenAiKey(k: String) = ss.saveString(SecureStorage.KEY_OPENAI_KEY, k)
    fun getOpenAiModel(): String = ss.getString(SecureStorage.KEY_OPENAI_MODEL, "gpt-4o-mini")
    fun setOpenAiModel(m: String) = ss.saveString(SecureStorage.KEY_OPENAI_MODEL, m)
    fun getGeminiKey(): String = ss.getString(SecureStorage.KEY_GEMINI_KEY)
    fun setGeminiKey(k: String) = ss.saveString(SecureStorage.KEY_GEMINI_KEY, k)
    fun getGeminiModel(): String = ss.getString(SecureStorage.KEY_GEMINI_MODEL, "gemini-2.0-flash")
    fun setGeminiModel(m: String) = ss.saveString(SecureStorage.KEY_GEMINI_MODEL, m)
    fun getAgentRouterKey(): String = ss.getString(SecureStorage.KEY_AGENT_ROUTER_KEY)
    fun setAgentRouterKey(k: String) = ss.saveString(SecureStorage.KEY_AGENT_ROUTER_KEY, k)
    fun getAgentRouterModel(): String = ss.getString(SecureStorage.KEY_AGENT_ROUTER_MODEL, "gpt-4o-mini")
    fun setAgentRouterModel(m: String) = ss.saveString(SecureStorage.KEY_AGENT_ROUTER_MODEL, m)
    fun getAgentRouterEndpoint(): String = ss.getString(SecureStorage.KEY_AGENT_ROUTER_ENDPOINT, "https://agentrouter.org")
    fun setAgentRouterEndpoint(e: String) = ss.saveString(SecureStorage.KEY_AGENT_ROUTER_ENDPOINT, e)
    fun getWakeWord(): String = ss.getString(SecureStorage.KEY_WAKE_WORD, "Hey Jarvis")
    fun setWakeWord(w: String) = ss.saveString(SecureStorage.KEY_WAKE_WORD, w)
    fun isWakeWordEnabled(): Boolean = ss.getBoolean(SecureStorage.KEY_WAKE_WORD_ENABLED, false)
    fun setWakeWordEnabled(e: Boolean) = ss.saveBoolean(SecureStorage.KEY_WAKE_WORD_ENABLED, e)
    fun isOverlayEnabled(): Boolean = ss.getBoolean(SecureStorage.KEY_OVERLAY_ENABLED, false)
    fun setOverlayEnabled(e: Boolean) = ss.saveBoolean(SecureStorage.KEY_OVERLAY_ENABLED, e)
    fun isVoiceEnabled(): Boolean = ss.getBoolean(SecureStorage.KEY_VOICE_ENABLED, true)
    fun setVoiceEnabled(e: Boolean) = ss.saveBoolean(SecureStorage.KEY_VOICE_ENABLED, e)
    fun isContinuousListening(): Boolean = ss.getBoolean(SecureStorage.KEY_CONTINUOUS_LISTENING, false)
    fun setContinuousListening(e: Boolean) = ss.saveBoolean(SecureStorage.KEY_CONTINUOUS_LISTENING, e)
    fun isAnnounceCalls(): Boolean = ss.getBoolean(SecureStorage.KEY_ANNOUNCE_CALLS, false)
    fun setAnnounceCalls(e: Boolean) = ss.saveBoolean(SecureStorage.KEY_ANNOUNCE_CALLS, e)
    fun isAutoAnswer(): Boolean = ss.getBoolean(SecureStorage.KEY_AUTO_ANSWER, false)
    fun setAutoAnswer(e: Boolean) = ss.saveBoolean(SecureStorage.KEY_AUTO_ANSWER, e)
    fun isWeatherEnabled(): Boolean = ss.getBoolean(SecureStorage.KEY_WEATHER_ENABLED, false)
    fun setWeatherEnabled(e: Boolean) = ss.saveBoolean(SecureStorage.KEY_WEATHER_ENABLED, e)
    fun isOnboardingComplete(): Boolean = ss.getBoolean(SecureStorage.KEY_ONBOARDING_COMPLETE, false)
    fun setOnboardingComplete(c: Boolean) = ss.saveBoolean(SecureStorage.KEY_ONBOARDING_COMPLETE, c)
}
