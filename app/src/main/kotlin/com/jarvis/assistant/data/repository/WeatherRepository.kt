package com.jarvis.assistant.data.repository

import com.jarvis.assistant.data.remote.WeatherApiService
import com.jarvis.assistant.domain.model.WeatherData
import com.jarvis.assistant.util.WeatherCodeMapper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(private val api: WeatherApiService) {
    suspend fun getWeather(lat: Double, lon: Double): Result<WeatherData> = try {
        val res = api.getWeather(lat, lon)
        if (res.isSuccessful && res.body()?.current != null) {
            val c = res.body()!!.current!!
            Result.success(WeatherData(c.temperature_2m, c.apparent_temperature, c.relative_humidity_2m, c.wind_speed_10m, c.weather_code, WeatherCodeMapper.getDescription(c.weather_code), c.is_day == 1))
        } else Result.failure(Exception("Weather API error: ${res.code()}"))
    } catch (e: Exception) { Result.failure(e) }
}
