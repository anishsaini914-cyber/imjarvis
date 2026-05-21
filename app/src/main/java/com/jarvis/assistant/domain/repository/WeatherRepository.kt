package com.jarvis.assistant.domain.repository

import com.jarvis.assistant.domain.model.WeatherData

interface WeatherRepository {
    suspend fun getWeather(lat: Double, lon: Double): Result<WeatherData>
}
