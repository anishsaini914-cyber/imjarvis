package com.jarvis.assistant.domain.model

data class AppCommand(val command: String, val action: AppAction, val params: Map<String, String> = emptyMap())

enum class AppAction {
    OPEN_APP, OPEN_SETTINGS, TOGGLE_FLASHLIGHT, SET_ALARM, READ_NOTIFICATIONS, SEND_WHATSAPP,
    OPEN_BROWSER, PLAY_MUSIC, CHECK_BATTERY, GET_WEATHER, MAKE_CALL, ANSWER_CALL, REJECT_CALL,
    SPEAKER_MODE, MUTE_CALL, UNMUTE_CALL, UNKNOWN
}
