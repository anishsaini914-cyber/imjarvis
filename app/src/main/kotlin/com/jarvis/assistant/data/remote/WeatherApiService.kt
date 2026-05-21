package com.jarvis.assistant.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("v1/forecast")
    suspend fun getWeather(@Query("latitude") lat: Double, @Query("longitude") lon: Double, @Query("current") current: String = "temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,wind_speed_10m,is_day"): Response<WeatherResponse>
}

data class WeatherResponse(val latitude: Double = 0.0, val longitude: Double = 0.0, val current: CurrentWeather? = null)
data class CurrentWeather(val temperature_2m: Double = 0.0, val relative_humidity_2m: Double = 0.0, val apparent_temperature: Double = 0.0, val weather_code: Int = 0, val wind_speed_10m: Double = 0.0, val is_day: Int = 1)
