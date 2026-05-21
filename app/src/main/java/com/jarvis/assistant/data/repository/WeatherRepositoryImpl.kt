package com.jarvis.assistant.data.repository

import com.jarvis.assistant.data.api.OpenMeteoApi
import com.jarvis.assistant.domain.model.WeatherData
import com.jarvis.assistant.domain.repository.WeatherRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val openMeteoApi: OpenMeteoApi
) : WeatherRepository {

    override suspend fun getWeather(lat: Double, lon: Double): Result<WeatherData> {
        return try {
            val response = openMeteoApi.getForecast(latitude = lat, longitude = lon)
            val current = response.current ?: return Result.failure(Exception("No weather data"))
            Result.success(WeatherData(
                temperature = current.temperature_2m ?: 0.0,
                feelsLike = current.apparent_temperature ?: 0.0,
                humidity = current.relative_humidity_2m ?: 0,
                windSpeed = current.wind_speed_10m ?: 0.0,
                weatherCode = current.weather_code ?: 0,
                weatherDescription = getWeatherDescription(current.weather_code ?: 0),
                location = "${response.latitude}, ${response.longitude}"
            ))
        } catch (e: Exception) { Result.failure(e) }
    }

    private fun getWeatherDescription(code: Int): String = when (code) {
        0 -> "Clear sky"; 1,2,3 -> "Mainly clear"; 45,48 -> "Foggy"
        51,53,55 -> "Drizzle"; 56,57 -> "Freezing drizzle"; 61,63,65 -> "Rain"
        66,67 -> "Freezing rain"; 71,73,75 -> "Snowfall"; 77 -> "Snow grains"
        80,81,82 -> "Rain showers"; 85,86 -> "Snow showers"; 95 -> "Thunderstorm"
        96,99 -> "Thunderstorm with hail"; else -> "Unknown"
    }
}
