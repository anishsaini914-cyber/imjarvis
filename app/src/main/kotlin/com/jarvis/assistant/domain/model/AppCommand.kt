package com.jarvis.assistant.domain.model

sealed class AppCommand {
    data class OpenApp(val pkg: String) : AppCommand()
    data object FlashlightOn : AppCommand()
    data object FlashlightOff : AppCommand()
    data object GetWeather : AppCommand()
    data object GetBattery : AppCommand()
    data object Unknown : AppCommand()
}
