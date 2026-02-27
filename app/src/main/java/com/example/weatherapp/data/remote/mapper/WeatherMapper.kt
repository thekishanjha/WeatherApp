package com.example.weatherapp.data.remote.mapper

import com.example.weatherapp.data.remote.dto.WeatherResponseDto
import com.example.weatherapp.domain.model.WeatherInfo

fun WeatherResponseDto.toWeatherInfo(): WeatherInfo {
    return WeatherInfo(
        cityName = cityName,
        temperature = main.temperature,
        feelsLike = main.feelsLike,
        description = weather.firstOrNull()?.description ?: "Unknown",
        humidity = main.humidity,
        windSpeed = wind.speed,
        pressure = main.pressure,
        visibility = visibility,
        icon = weather.firstOrNull()?.icon ?: ""
    )
}