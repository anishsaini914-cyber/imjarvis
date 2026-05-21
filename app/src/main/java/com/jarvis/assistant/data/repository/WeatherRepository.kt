package com.jarvis.assistant.data.repository

import android.content.Context
import android.location.LocationManager
import com.jarvis.assistant.data.remote.WeatherApi
import com.jarvis.assistant.data.remote.dto.getWeatherEmoji
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepository @Inject constructor(
    private val weatherApi: WeatherApi,
    private val context: Context
) {
    suspend fun getWeather(): Result<WeatherInfo> {
        val loc = getLastKnownLocation() ?: return Result.failure(Exception("Location not available"))
        return try {
            val resp = weatherApi.getForecast(loc.first, loc.second)
            val cur = resp.currentWeather ?: return Result.failure(Exception("Weather data not available"))
            Result.success(WeatherInfo(temperature = cur.temperature ?: 0.0, weatherCode = cur.weathercode, emoji = getWeatherEmoji(cur.weathercode), description = describe(cur.weathercode)))
        } catch (e: Exception) { Result.failure(e) }
    }

    private fun getLastKnownLocation(): Pair<Double, Double>? {
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return null
        return lm.getProviders(true).firstNotNullOfOrNull { lm.getLastKnownLocation(it) }?.let { Pair(it.latitude, it.longitude) }
    }

    private fun describe(code: Int?): String = when (code) {
        0 -> "Clear"; 1 -> "Mainly Clear"; 2 -> "Partly Cloudy"; 3 -> "Overcast"
        45,48 -> "Foggy"; 51,53,55 -> "Drizzle"; 61,63,65 -> "Rain"
        71,73,75 -> "Snow"; 80,81,82 -> "Rain Showers"; 95 -> "Thunderstorm"
        else -> "Unknown"
    }

    data class WeatherInfo(val temperature: Double, val weatherCode: Int?, val emoji: String, val description: String)
}
