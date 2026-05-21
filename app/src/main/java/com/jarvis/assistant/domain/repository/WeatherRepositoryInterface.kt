package com.jarvis.assistant.domain.repository

import com.jarvis.assistant.domain.model.WeatherData

interface WeatherRepositoryInterface { suspend fun getWeather(latitude: Double, longitude: Double, unit: String): Result<WeatherData> }
