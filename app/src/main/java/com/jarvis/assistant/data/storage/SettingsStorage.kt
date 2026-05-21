package com.jarvis.assistant.data.storage

import android.content.SharedPreferences
import com.jarvis.assistant.domain.model.AIProvider
import com.jarvis.assistant.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsStorage @Inject constructor(
    private val securePrefs: SharedPreferences,
    private val prefs: SharedPreferences
) {
    private val _activeProvider = MutableStateFlow(
        AIProvider.fromId(prefs.getString(Constants.KEY_ACTIVE_PROVIDER, AIProvider.OPENAI.id) ?: AIProvider.OPENAI.id)
    )
    private val _wakeWordEnabled = MutableStateFlow(prefs.getBoolean(Constants.KEY_WAKE_WORD_ENABLED, false))
    private val _wakeWordPhrase = MutableStateFlow(
        prefs.getString(Constants.KEY_WAKE_WORD_PHRASE, "hey jarvis") ?: "hey jarvis"
    )
    private val _overlayEnabled = MutableStateFlow(prefs.getBoolean(Constants.KEY_OVERLAY_ENABLED, false))
    private val _theme = MutableStateFlow(
        prefs.getString(Constants.KEY_THEME, "dark") ?: "dark"
    )
    private val _language = MutableStateFlow(
        prefs.getString(Constants.KEY_LANGUAGE, "en-US") ?: "en-US"
    )
    private val _onboardingCompleted = MutableStateFlow(prefs.getBoolean(Constants.KEY_ONBOARDING_COMPLETED, false))
    private val _openaiApiKey = MutableStateFlow(securePrefs.getString(Constants.KEY_OPENAI_API_KEY, "") ?: "")
    private val _openaiModel = MutableStateFlow(
        prefs.getString(Constants.KEY_OPENAI_MODEL, Constants.DEFAULT_OPENAI_MODEL) ?: Constants.DEFAULT_OPENAI_MODEL
    )
    private val _openaiEndpoint = MutableStateFlow(
        prefs.getString(Constants.KEY_OPENAI_ENDPOINT, Constants.DEFAULT_OPENAI_ENDPOINT) ?: Constants.DEFAULT_OPENAI_ENDPOINT
    )
    private val _geminiApiKey = MutableStateFlow(securePrefs.getString(Constants.KEY_GEMINI_API_KEY, "") ?: "")
    private val _geminiModel = MutableStateFlow(
        prefs.getString(Constants.KEY_GEMINI_MODEL, Constants.DEFAULT_GEMINI_MODEL) ?: Constants.DEFAULT_GEMINI_MODEL
    )
    private val _agentrouterApiKey = MutableStateFlow(securePrefs.getString(Constants.KEY_AGENTROUTER_API_KEY, "") ?: "")
    private val _agentrouterEndpoint = MutableStateFlow(
        prefs.getString(Constants.KEY_AGENTROUTER_ENDPOINT, Constants.DEFAULT_AGENTROUTER_ENDPOINT) ?: Constants.DEFAULT_AGENTROUTER_ENDPOINT
    )
    private val _agentrouterModel = MutableStateFlow(prefs.getString(Constants.KEY_AGENTROUTER_MODEL, "") ?: "")

    fun getActiveProvider(): Flow<AIProvider> = _activeProvider.asStateFlow()
    fun setActiveProvider(p: AIProvider) { _activeProvider.value = p; prefs.edit().putString(Constants.KEY_ACTIVE_PROVIDER, p.id).apply() }
    fun getOpenAIApiKey(): Flow<String> = _openaiApiKey.asStateFlow()
    fun setOpenAIApiKey(k: String) { _openaiApiKey.value = k; securePrefs.edit().putString(Constants.KEY_OPENAI_API_KEY, k).apply() }
    fun getOpenAIModel(): Flow<String> = _openaiModel.asStateFlow()
    fun setOpenAIModel(m: String) { _openaiModel.value = m; prefs.edit().putString(Constants.KEY_OPENAI_MODEL, m).apply() }
    fun getOpenAIEndpoint(): Flow<String> = _openaiEndpoint.asStateFlow()
    fun setOpenAIEndpoint(e: String) { _openaiEndpoint.value = e; prefs.edit().putString(Constants.KEY_OPENAI_ENDPOINT, e).apply() }
    fun getGeminiApiKey(): Flow<String> = _geminiApiKey.asStateFlow()
    fun setGeminiApiKey(k: String) { _geminiApiKey.value = k; securePrefs.edit().putString(Constants.KEY_GEMINI_API_KEY, k).apply() }
    fun getGeminiModel(): Flow<String> = _geminiModel.asStateFlow()
    fun setGeminiModel(m: String) { _geminiModel.value = m; prefs.edit().putString(Constants.KEY_GEMINI_MODEL, m).apply() }
    fun getAgentRouterApiKey(): Flow<String> = _agentrouterApiKey.asStateFlow()
    fun setAgentRouterApiKey(k: String) { _agentrouterApiKey.value = k; securePrefs.edit().putString(Constants.KEY_AGENTROUTER_API_KEY, k).apply() }
    fun getAgentRouterEndpoint(): Flow<String> = _agentrouterEndpoint.asStateFlow()
    fun setAgentRouterEndpoint(e: String) { _agentrouterEndpoint.value = e; prefs.edit().putString(Constants.KEY_AGENTROUTER_ENDPOINT, e).apply() }
    fun getAgentRouterModel(): Flow<String> = _agentrouterModel.asStateFlow()
    fun setAgentRouterModel(m: String) { _agentrouterModel.value = m; prefs.edit().putString(Constants.KEY_AGENTROUTER_MODEL, m).apply() }
    fun isWakeWordEnabled(): Flow<Boolean> = _wakeWordEnabled.asStateFlow()
    fun setWakeWordEnabled(e: Boolean) { _wakeWordEnabled.value = e; prefs.edit().putBoolean(Constants.KEY_WAKE_WORD_ENABLED, e).apply() }
    fun getWakeWordPhrase(): Flow<String> = _wakeWordPhrase.asStateFlow()
    fun setWakeWordPhrase(p: String) { _wakeWordPhrase.value = p; prefs.edit().putString(Constants.KEY_WAKE_WORD_PHRASE, p).apply() }
    fun isOverlayEnabled(): Flow<Boolean> = _overlayEnabled.asStateFlow()
    fun setOverlayEnabled(e: Boolean) { _overlayEnabled.value = e; prefs.edit().putBoolean(Constants.KEY_OVERLAY_ENABLED, e).apply() }
    fun getTheme(): Flow<String> = _theme.asStateFlow()
    fun setTheme(t: String) { _theme.value = t; prefs.edit().putString(Constants.KEY_THEME, t).apply() }
    fun getLanguage(): Flow<String> = _language.asStateFlow()
    fun setLanguage(l: String) { _language.value = l; prefs.edit().putString(Constants.KEY_LANGUAGE, l).apply() }
    fun isOnboardingCompleted(): Flow<Boolean> = _onboardingCompleted.asStateFlow()
    fun setOnboardingCompleted(c: Boolean) { _onboardingCompleted.value = c; prefs.edit().putBoolean(Constants.KEY_ONBOARDING_COMPLETED, c).apply() }

    fun getOpenAIApiKeySync(): String = _openaiApiKey.value
    fun getGeminiApiKeySync(): String = _geminiApiKey.value
    fun getAgentRouterApiKeySync(): String = _agentrouterApiKey.value
    fun getActiveProviderSync(): AIProvider = _activeProvider.value
    fun getOpenAIModelSync(): String = _openaiModel.value
    fun getGeminiModelSync(): String = _geminiModel.value
    fun getAgentRouterModelSync(): String = _agentrouterModel.value
    fun getOpenAIEndpointSync(): String = _openaiEndpoint.value
    fun getAgentRouterEndpointSync(): String = _agentrouterEndpoint.value
    fun getWakeWordSensitivity(): Flow<Int> = MutableStateFlow(prefs.getInt(Constants.KEY_WAKE_WORD_SENSITIVITY, 70)).asStateFlow()
    fun setWakeWordSensitivity(s: Int) { prefs.edit().putInt(Constants.KEY_WAKE_WORD_SENSITIVITY, s).apply() }
    fun getWakeWordPhraseSync(): String = _wakeWordPhrase.value
}
