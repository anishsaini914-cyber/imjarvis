package com.jarvis.assistant.data.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.jarvis.assistant.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecureStorage @Inject constructor(@ApplicationContext private val context: Context) {
    private val prefs: SharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        EncryptedSharedPreferences.create(context, Constants.PREFS_NAME, masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
    }

    fun getString(key: String, default: String? = null): String? = prefs.getString(key, default)
    fun putString(key: String, value: String) { prefs.edit().putString(key, value).apply() }
    fun getBoolean(key: String, default: Boolean = false): Boolean = prefs.getBoolean(key, default)
    fun putBoolean(key: String, value: Boolean) { prefs.edit().putBoolean(key, value).apply() }
    fun getFloat(key: String, default: Float = 0f): Float = prefs.getFloat(key, default)
    fun putFloat(key: String, value: Float) { prefs.edit().putFloat(key, value).apply() }
    fun remove(key: String) { prefs.edit().remove(key).apply() }
    fun clear() { prefs.edit().clear().apply() }
    fun getOpenAiKey(): String? = getString(Constants.KEY_OPENAI_KEY)
    fun setOpenAiKey(key: String) = putString(Constants.KEY_OPENAI_KEY, key)
    fun getGeminiKey(): String? = getString(Constants.KEY_GEMINI_KEY)
    fun setGeminiKey(key: String) = putString(Constants.KEY_GEMINI_KEY, key)
    fun getAgentRouterKey(): String? = getString(Constants.KEY_AGENT_ROUTER_KEY)
    fun setAgentRouterKey(key: String) = putString(Constants.KEY_AGENT_ROUTER_KEY, key)
}
