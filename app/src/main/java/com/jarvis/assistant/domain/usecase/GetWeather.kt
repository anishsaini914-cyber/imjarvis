package com.jarvis.assistant.domain.usecase

import com.jarvis.assistant.domain.model.WeatherData
import com.jarvis.assistant.domain.repository.WeatherRepositoryInterface
import javax.inject.Inject

class GetWeather @Inject constructor(private val weatherRepository: WeatherRepositoryInterface) {
    suspend operator fun invoke(latitude: Double, longitude: Double, unit: String = "celsius"): Result<WeatherData> =
        weatherRepository.getWeather(latitude, longitude, unit)
}
