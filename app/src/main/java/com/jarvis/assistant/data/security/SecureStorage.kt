package com.jarvis.assistant.data.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecureStorage(context: Context) {
    private val masterKey = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
    private val prefs: SharedPreferences = EncryptedSharedPreferences.create(context, "jarvis_secure_prefs", masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV, EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)

    fun saveString(k: String, v: String) { prefs.edit().putString(k, v).apply() }
    fun getString(k: String, d: String = ""): String = prefs.getString(k, d) ?: d
    fun saveBoolean(k: String, v: Boolean) { prefs.edit().putBoolean(k, v).apply() }
    fun getBoolean(k: String, d: Boolean = false): Boolean = prefs.getBoolean(k, d)
    fun remove(k: String) { prefs.edit().remove(k).apply() }
    fun clear() { prefs.edit().clear().apply() }

    companion object {
        const val KEY_OPENAI_KEY = "openai_api_key"
        const val KEY_OPENAI_MODEL = "openai_model"
        const val KEY_GEMINI_KEY = "gemini_api_key"
        const val KEY_GEMINI_MODEL = "gemini_model"
        const val KEY_AGENT_ROUTER_KEY = "agent_router_api_key"
        const val KEY_AGENT_ROUTER_MODEL = "agent_router_model"
        const val KEY_AGENT_ROUTER_ENDPOINT = "agent_router_endpoint"
        const val KEY_ACTIVE_PROVIDER = "active_provider"
        const val KEY_WAKE_WORD = "wake_word"
        const val KEY_WAKE_WORD_ENABLED = "wake_word_enabled"
        const val KEY_OVERLAY_ENABLED = "overlay_enabled"
        const val KEY_VOICE_ENABLED = "voice_enabled"
        const val KEY_CONTINUOUS_LISTENING = "continuous_listening"
        const val KEY_ANNOUNCE_CALLS = "announce_calls"
        const val KEY_AUTO_ANSWER = "auto_answer"
        const val KEY_WEATHER_ENABLED = "weather_enabled"
        const val KEY_THEME = "theme"
        const val KEY_LANGUAGE = "language"
        const val KEY_ONBOARDING_COMPLETE = "onboarding_complete"
        const val KEY_LOCAL_MODEL_PATH = "local_model_path"
        const val KEY_LOCAL_MODEL_ENABLED = "local_model_enabled"
    }
}
