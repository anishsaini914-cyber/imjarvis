package com.jarvis.assistant.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val latitude: Double? = null,
    val longitude: Double? = null,
    @SerializedName("current_weather") val currentWeather: CurrentWeather? = null,
    val daily: DailyWeather? = null
)
data class CurrentWeather(val temperature: Double? = null, val windspeed: Double? = null, val weathercode: Int? = null, val time: String? = null)
data class DailyWeather(val time: List<String>? = null, @SerializedName("temperature_2m_max") val tempMax: List<Double>? = null, @SerializedName("temperature_2m_min") val tempMin: List<Double>? = null, @SerializedName("weathercode") val weatherCode: List<Int>? = null)

fun getWeatherEmoji(code: Int?): String = when (code) {
    0 -> "\u2600\uFE0F"; 1,2,3 -> "\u26C5"; 45,48 -> "\uF303"
    51,53,55 -> "\uD83C\uDF26\uFE0F"; 56,57 -> "\uD83C\uDF27\uFE0F"
    61,63,65 -> "\uD83C\uDF27\uFE0F"; 66,67 -> "\uD83C\uDF28\uFE0F"
    71,73,75 -> "\u2744\uFE0F"; 77 -> "\uD83C\uDF28\uFE0F"
    80,81,82 -> "\uD83C\uDF27\uFE0F"; 85,86 -> "\u2744\uFE0F"
    95 -> "\u26A1"; 96,99 -> "\u26A1"
    else -> "\uD83C\uDF24\uFE0F"
}
