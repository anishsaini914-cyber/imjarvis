package com.jarvis.assistant.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.jarvis.assistant.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("jarvis_prefs", Context.MODE_PRIVATE)

    fun getString(key: String, default: String = ""): String = prefs.getString(key, default) ?: default
    fun putString(key: String, value: String) { prefs.edit().putString(key, value).apply() }
    fun getBoolean(key: String, default: Boolean = false): Boolean = prefs.getBoolean(key, default)
    fun putBoolean(key: String, value: Boolean) { prefs.edit().putBoolean(key, value).apply() }
    fun getInt(key: String, default: Int = 0): Int = prefs.getInt(key, default)
    fun putInt(key: String, value: Int) { prefs.edit().putInt(key, value).apply() }
    fun getFloat(key: String, default: Float = 0f): Float = prefs.getFloat(key, default)
    fun putFloat(key: String, value: Float) { prefs.edit().putFloat(key, value).apply() }
    fun remove(key: String) { prefs.edit().remove(key).apply() }
    fun clear() { prefs.edit().clear().apply() }

    var selectedProvider: String
        get() = getString(Constants.KEY_SELECTED_PROVIDER, "openai")
        set(value) = putString(Constants.KEY_SELECTED_PROVIDER, value)
    var openAiModel: String
        get() = getString(Constants.KEY_OPENAI_MODEL, "gpt-4o-mini")
        set(value) = putString(Constants.KEY_OPENAI_MODEL, value)
    var geminiModel: String
        get() = getString(Constants.KEY_GEMINI_MODEL, "gemini-pro")
        set(value) = putString(Constants.KEY_GEMINI_MODEL, value)
    var agentRouterModel: String
        get() = getString(Constants.KEY_AGENT_ROUTER_MODEL, "gpt-4o-mini")
        set(value) = putString(Constants.KEY_AGENT_ROUTER_MODEL, value)
    var wakeWord: String
        get() = getString(Constants.KEY_WAKE_WORD, Constants.DEFAULT_WAKE_WORD)
        set(value) = putString(Constants.KEY_WAKE_WORD, value)
    var wakeWordEnabled: Boolean
        get() = getBoolean(Constants.KEY_WAKE_WORD_ENABLED, false)
        set(value) = putBoolean(Constants.KEY_WAKE_WORD_ENABLED, value)
    var overlayEnabled: Boolean
        get() = getBoolean(Constants.KEY_OVERLAY_ENABLED, false)
        set(value) = putBoolean(Constants.KEY_OVERLAY_ENABLED, value)
    var ttsEnabled: Boolean
        get() = getBoolean(Constants.KEY_TTS_ENABLED, true)
        set(value) = putBoolean(Constants.KEY_TTS_ENABLED, value)
    var callAnnouncement: Boolean
        get() = getBoolean(Constants.KEY_CALL_ANNOUNCEMENT, true)
        set(value) = putBoolean(Constants.KEY_CALL_ANNOUNCEMENT, value)
    var weatherUnit: String
        get() = getString(Constants.KEY_WEATHER_UNIT, "celsius")
        set(value) = putString(Constants.KEY_WEATHER_UNIT, value)
    var latitude: Float
        get() = getFloat(Constants.KEY_LATITUDE, 28.6139f)
        set(value) = putFloat(Constants.KEY_LATITUDE, value)
    var longitude: Float
        get() = getFloat(Constants.KEY_LONGITUDE, 77.2090f)
        set(value) = putFloat(Constants.KEY_LONGITUDE, value)
    var onboardingDone: Boolean
        get() = getBoolean(Constants.KEY_ONBOARDING_DONE, false)
        set(value) = putBoolean(Constants.KEY_ONBOARDING_DONE, value)
}
