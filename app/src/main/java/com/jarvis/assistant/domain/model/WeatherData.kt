package com.jarvis.assistant.domain.model

data class WeatherData(val temperature: Double = 0.0, val feelsLike: Double = 0.0, val humidity: Double = 0.0,
    val windSpeed: Double = 0.0, val weatherCode: Int = 0, val weatherDescription: String = "",
    val dailyForecast: List<DailyForecast> = emptyList())
data class DailyForecast(val date: String, val maxTemp: Double, val minTemp: Double, val weatherCode: Int, val weatherDescription: String)
