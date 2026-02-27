package com.example.weatherapp.domain.usecase

import com.example.weatherapp.domain.model.City
import com.example.weatherapp.domain.repository.WeatherRepository
import javax.inject.Inject

class SaveCityUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(city: City): Result<Unit> {
        return weatherRepository.saveCity(city)
    }
}