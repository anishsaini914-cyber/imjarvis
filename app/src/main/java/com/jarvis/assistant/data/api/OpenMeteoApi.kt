package com.jarvis.assistant.data.api

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenMeteoApi {
    @GET("v1/forecast")
    suspend fun getForecast(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,wind_speed_10m"
    ): OpenMeteoResponse
}

data class OpenMeteoResponse(
    val latitude: Double, val longitude: Double, val current: CurrentWeather?
)

data class CurrentWeather(
    val temperature_2m: Double?, val relative_humidity_2m: Int?,
    val apparent_temperature: Double?, val weather_code: Int?, val wind_speed_10m: Double?
)
