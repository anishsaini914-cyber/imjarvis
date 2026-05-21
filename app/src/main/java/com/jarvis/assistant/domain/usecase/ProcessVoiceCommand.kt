package com.jarvis.assistant.domain.usecase

import com.jarvis.assistant.domain.model.AppAction
import com.jarvis.assistant.domain.model.AppCommand

class ProcessVoiceCommand {
    fun execute(input: String): AppCommand {
        val lower = input.lowercase().trim()
        if (lower.contains("who is calling") || lower.contains("kaun call")) return AppCommand("who_is_calling", AppAction.READ_NOTIFICATIONS)
        if (lower.contains("call uthao") || lower.contains("answer call") || lower.contains("pick up")) return AppCommand("answer_call", AppAction.ANSWER_CALL)
        if (lower.contains("call cut") || lower.contains("reject call") || lower.contains("call band")) return AppCommand("reject_call", AppAction.REJECT_CALL)
        if (lower.contains("speaker") || lower.contains("speaker pe")) return AppCommand("speaker_mode", AppAction.SPEAKER_MODE)
        if (lower.contains("flashlight") || lower.contains("torch") || lower.contains("flash")) return AppCommand("toggle_flashlight", AppAction.TOGGLE_FLASHLIGHT)
        if (lower.contains("weather") || lower.contains("mausam") || lower.contains("temperature")) return AppCommand("get_weather", AppAction.GET_WEATHER)
        if (lower.contains("battery") || lower.contains("charge")) return AppCommand("check_battery", AppAction.CHECK_BATTERY)
        if (lower.contains("notification") || lower.contains("read notification")) return AppCommand("read_notifications", AppAction.READ_NOTIFICATIONS)
        if (lower.contains("open whatsapp")) return AppCommand("open_whatsapp", AppAction.OPEN_APP, mapOf("package" to "com.whatsapp"))
        if (lower.contains("open youtube")) return AppCommand("open_youtube", AppAction.OPEN_APP, mapOf("package" to "com.google.android.youtube"))
        if (lower.contains("open camera")) return AppCommand("open_camera", AppAction.OPEN_APP, mapOf("package" to "com.android.camera"))
        if (lower.contains("open ")) return AppCommand("open_app", AppAction.OPEN_APP, mapOf("app_name" to lower.replace("open ", "").trim()))
        if (lower.contains("search ")) return AppCommand("browser_search", AppAction.OPEN_BROWSER, mapOf("query" to lower.replace("search ", "").trim()))
        if (lower.contains("play music") || lower.contains("play song") || lower.contains("gaana")) return AppCommand("play_music", AppAction.PLAY_MUSIC)
        return AppCommand("chat", AppAction.UNKNOWN, mapOf("text" to input))
    }
}
