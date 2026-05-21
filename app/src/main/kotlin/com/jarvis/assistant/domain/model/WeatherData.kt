package com.jarvis.assistant.domain.model

data class WeatherData(val temperature: Double, val feelsLike: Double, val humidity: Double, val windSpeed: Double, val weatherCode: Int, val weatherDescription: String, val isDay: Boolean)
