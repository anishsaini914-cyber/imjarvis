package com.jarvis.assistant.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import com.jarvis.assistant.domain.model.VoiceCommand
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoiceCommandParser @Inject constructor(@ApplicationContext private val context: Context) {

    fun parse(text: String): VoiceCommand {
        val lower = text.lowercase().trim()
        return when {
            lower.contains("who is calling") || lower.contains("kaun call") -> VoiceCommand.WhoIsCalling
            lower.contains("call uthao") || lower.contains("answer call") -> VoiceCommand.AcceptCall
            lower.contains("call cut") || lower.contains("reject call") -> VoiceCommand.RejectCall
            lower.contains("speaker") -> VoiceCommand.SpeakerOn
            lower.contains("flashlight on") || lower.contains("torch on") -> VoiceCommand.FlashlightOn
            lower.contains("flashlight off") || lower.contains("torch off") -> VoiceCommand.FlashlightOff
            lower.contains("weather") || lower.contains("mausam") -> VoiceCommand.CheckWeather
            lower.contains("battery") || lower.contains("charge") -> VoiceCommand.CheckBattery
            lower.contains("whatsapp") -> VoiceCommand.OpenWhatsApp
            lower.contains("music") || lower.contains("song") || lower.contains("gaana") -> VoiceCommand.OpenMusic
            lower.contains("search") || lower.contains("google") -> VoiceCommand.SearchWeb(extractQuery(lower))
            lower.contains("notification") -> VoiceCommand.ReadNotifications
            lower.startsWith("open ") -> VoiceCommand.OpenApp(lower.removePrefix("open ").trim())
            lower.contains("settings") -> VoiceCommand.OpenSettings(null)
            else -> VoiceCommand.Unknown
        }
    }

    fun executeCommand(command: VoiceCommand, activity: android.app.Activity? = null): String {
        return when (command) {
            is VoiceCommand.OpenApp -> openApp(command.appName)
            is VoiceCommand.OpenSettings -> openSettings()
            is VoiceCommand.FlashlightOn -> "Flashlight turned on"
            is VoiceCommand.FlashlightOff -> "Flashlight turned off"
            is VoiceCommand.CheckWeather -> "Checking weather..."
            is VoiceCommand.CheckBattery -> checkBattery()
            is VoiceCommand.OpenWhatsApp -> openApp("whatsapp")
            is VoiceCommand.OpenMusic -> openMusic()
            is VoiceCommand.SearchWeb -> searchWeb(command.query)
            is VoiceCommand.WhoIsCalling -> "Checking who is calling..."
            is VoiceCommand.AcceptCall -> "Accepting call"
            is VoiceCommand.RejectCall -> "Rejecting call"
            is VoiceCommand.SpeakerOn -> "Turning on speaker"
            else -> "I didn't understand that command"
        }
    }

    private fun openApp(appName: String): String {
        val pkg = when {
            appName.contains("whatsapp") -> "com.whatsapp"
            appName.contains("youtube") -> "com.google.android.youtube"
            appName.contains("chrome") || appName.contains("browser") -> "com.android.chrome"
            appName.contains("camera") -> "com.google.android.GoogleCamera"
            appName.contains("phone") || appName.contains("dialer") -> "com.google.android.dialer"
            appName.contains("messages") || appName.contains("sms") -> "com.google.android.apps.messaging"
            appName.contains("gmail") || appName.contains("mail") -> "com.google.android.gm"
            appName.contains("maps") -> "com.google.android.apps.maps"
            appName.contains("calculator") -> "com.google.android.calculator"
            appName.contains("clock") || appName.contains("alarm") -> "com.google.android.deskclock"
            appName.contains("music") || appName.contains("spotify") -> "com.spotify.music"
            appName.contains("instagram") -> "com.instagram.android"
            appName.contains("facebook") -> "com.facebook.katana"
            appName.contains("twitter") || appName.contains("x ") -> "com.twitter.android"
            appName.contains("telegram") -> "org.telegram.messenger"
            else -> appName
        }
        return try {
            context.packageManager.getLaunchIntentForPackage(pkg)?.apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(this)
                "Opening $appName"
            } ?: "Could not find $appName"
        } catch (_: Exception) { "Could not open $appName" }
    }

    private fun openSettings(): String = try {
        context.startActivity(Intent(Settings.ACTION_SETTINGS).apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
        "Opening settings"
    } catch (_: Exception) { "Could not open settings" }

    private fun checkBattery(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            val intent = context.registerReceiver(null, Intent(Intent.ACTION_BATTERY_CHANGED))
            val level = intent?.getIntExtra(android.os.BatteryManager.EXTRA_LEVEL, -1) ?: -1
            val scale = intent?.getIntExtra(android.os.BatteryManager.EXTRA_SCALE, -1) ?: -1
            return if (level >= 0 && scale > 0) "Battery at ${level * 100 / scale}%" else "Could not read battery"
        }
        return "Battery check not available"
    }

    private fun openMusic(): String {
        for (pkg in listOf("com.spotify.music", "com.google.android.apps.youtube.music")) {
            try {
                context.packageManager.getLaunchIntentForPackage(pkg)?.apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(this)
                    return "Opening music"
                }
            } catch (_: Exception) {}
        }
        return "No music app found"
    }

    private fun searchWeb(query: String): String = try {
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=${Uri.encode(query)}"))
            .apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) })
        "Searching for $query"
    } catch (_: Exception) { "Could not search" }

    private fun extractQuery(text: String): String = text.replace(Regex("(search|google)", RegexOption.IGNORE_CASE), "").trim()
}
