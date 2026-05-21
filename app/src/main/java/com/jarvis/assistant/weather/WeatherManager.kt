package com.jarvis.assistant.weather

import com.jarvis.assistant.domain.model.WeatherData
import com.jarvis.assistant.domain.usecase.GetWeather
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherManager @Inject constructor(private val getWeather: GetWeather) {
    suspend fun getCurrentWeather(lat: Double, lon: Double, unit: String = "celsius"): Result<WeatherData> = getWeather(lat, lon, unit)
    fun getWeatherEmoji(code: Int): String = when (code) {
        0 -> "\u2600\uFE0F"; 1, 2, 3 -> "\u26C5"; 45, 48 -> "\uD83C\uDF2B\uFE0F"
        51, 53, 55 -> "\uD83C\uDF26\uFE0F"; 61, 63, 65 -> "\uD83C\uDF27\uFE0F"
        71, 73, 75 -> "\u2744\uFE0F"; 80, 81, 82 -> "\uD83C\uDF27\uFE0F"
        95, 96, 99 -> "\u26C8\uFE0F"; else -> "\uD83C\uDF24\uFE0F"
    }
}
