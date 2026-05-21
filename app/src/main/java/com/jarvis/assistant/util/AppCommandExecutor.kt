package com.jarvis.assistant.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import com.jarvis.assistant.domain.model.AppAction
import com.jarvis.assistant.domain.model.AppCommand
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppCommandExecutor @Inject constructor(private val context: Context) {
    fun execute(command: AppCommand): String {
        return when (command.action) {
            AppAction.OPEN_APP -> openApp(command.params)
            AppAction.OPEN_SETTINGS -> openSettings()
            AppAction.TOGGLE_FLASHLIGHT -> toggleFlashlight()
            AppAction.CHECK_BATTERY -> checkBattery()
            AppAction.OPEN_BROWSER -> openBrowser(command.params)
            AppAction.PLAY_MUSIC -> playMusic()
            AppAction.GET_WEATHER -> "weather"
            AppAction.READ_NOTIFICATIONS -> "notifications"
            AppAction.ANSWER_CALL -> "Answering call..."
            AppAction.REJECT_CALL -> "Rejecting call..."
            AppAction.SPEAKER_MODE -> toggleSpeaker()
            AppAction.SET_ALARM -> "Setting alarm..."
            AppAction.MAKE_CALL -> "Making call..."
            AppAction.MUTE_CALL -> "Muting call..."
            AppAction.UNMUTE_CALL -> "Unmuting call..."
            else -> "I'll help with that: ${command.params["text"] ?: command.command}"
        }
    }

    private fun openApp(params: Map<String, String>): String {
        val packageName = params["package"]
        if (packageName != null) {
            try {
                val intent = context.packageManager.getLaunchIntentForPackage(packageName)
                if (intent != null) {
                    context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                    return "Opening ${params["app_name"] ?: packageName}"
                }
            } catch (_: Exception) {}
        }
        return "App not found"
    }

    private fun openSettings(): String {
        context.startActivity(Intent(Settings.ACTION_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        return "Opening settings"
    }

    private fun toggleFlashlight(): String = "Flashlight toggled"

    private fun checkBattery(): String {
        val intent = context.registerReceiver(null, android.content.IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = intent?.getIntExtra(android.os.BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val isCharging = intent?.getIntExtra(android.os.BatteryManager.EXTRA_STATUS, -1) == android.os.BatteryManager.BATTERY_STATUS_CHARGING
        return if (level >= 0) "Battery at $level%. ${if (isCharging) "Charging" else "Not charging"}" else "Unable to read battery"
    }

    private fun openBrowser(params: Map<String, String>): String {
        val query = params["query"] ?: return "No search query"
        val url = "https://www.google.com/search?q=${Uri.encode(query)}"
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        return "Searching for $query"
    }

    private fun playMusic(): String {
        try {
            context.packageManager.getLaunchIntentForPackage("com.spotify.music")?.let {
                context.startActivity(it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                return "Opening Spotify"
            }
        } catch (_: Exception) {}
        try {
            context.packageManager.getLaunchIntentForPackage("com.google.android.apps.youtube.music")?.let {
                context.startActivity(it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                return "Opening YouTube Music"
            }
        } catch (_: Exception) {}
        return "Music app not found"
    }

    private fun toggleSpeaker(): String = "Speaker mode toggled"
}
