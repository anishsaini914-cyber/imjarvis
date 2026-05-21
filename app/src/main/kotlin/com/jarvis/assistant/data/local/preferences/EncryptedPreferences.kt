package com.jarvis.assistant.data.local.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class EncryptedPreferences(context: Context) {
    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        "jarvis_secure_prefs",
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    var openAiApiKey: String by StringPref(prefs, "openai_api_key", "")
    var geminiApiKey: String by StringPref(prefs, "gemini_api_key", "")
    var agentRouterApiKey: String by StringPref(prefs, "agent_router_api_key", "")
    var activeProvider: String by StringPref(prefs, "active_provider", "openai")
    var openAiModel: String by StringPref(prefs, "openai_model", "gpt-4o")
    var geminiModel: String by StringPref(prefs, "gemini_model", "gemini-1.5-pro")
    var agentRouterModel: String by StringPref(prefs, "agent_router_model", "gpt-4o")
    var agentRouterEndpoint: String by StringPref(prefs, "agent_router_endpoint", "https://api.agentrouter.org/v1")
    var wakeWordEnabled: Boolean by BoolPref(prefs, "wake_word_enabled", false)
    var wakeWordPhrase: String by StringPref(prefs, "wake_word_phrase", "Hey Jarvis")
    var overlayEnabled: Boolean by BoolPref(prefs, "overlay_enabled", false)
    var ttsEnabled: Boolean by BoolPref(prefs, "tts_enabled", true)
    var voiceInputEnabled: Boolean by BoolPref(prefs, "voice_input_enabled", true)
    var weatherUnit: String by StringPref(prefs, "weather_unit", "celsius")
    var latitude: Double by DoublePref(prefs, "latitude", 28.6139)
    var longitude: Double by DoublePref(prefs, "longitude", 77.2090)
    var onboardingCompleted: Boolean by BoolPref(prefs, "onboarding_completed", false)
    var callHandlingEnabled: Boolean by BoolPref(prefs, "call_handling_enabled", false)
}
class StringPref(private val prefs: SharedPreferences, private val key: String, private val default: String) {
    operator fun getValue(thisRef: Any?, property: Any?) = prefs.getString(key, default) ?: default
    operator fun setValue(thisRef: Any?, property: Any?, value: String) = prefs.edit().putString(key, value).apply()
}
class BoolPref(private val prefs: SharedPreferences, private val key: String, private val default: Boolean) {
    operator fun getValue(thisRef: Any?, property: Any?) = prefs.getBoolean(key, default)
    operator fun setValue(thisRef: Any?, property: Any?, value: Boolean) = prefs.edit().putBoolean(key, value).apply()
}
class DoublePref(private val prefs: SharedPreferences, private val key: String, private val default: Double) {
    operator fun getValue(thisRef: Any?, property: Any?) = prefs.getFloat(key, default.toFloat()).toDouble()
    operator fun setValue(thisRef: Any?, property: Any?, value: Double) = prefs.edit().putFloat(key, value.toFloat()).apply()
}
