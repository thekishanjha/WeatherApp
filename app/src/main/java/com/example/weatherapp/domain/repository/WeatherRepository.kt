package com.example.weatherapp.domain.repository

import com.example.weatherapp.domain.model.City
import com.example.weatherapp.domain.model.WeatherInfo
import kotlinx.coroutines.flow.Flow

interface WeatherRepository{
    /**
     * Get weather information by city name
     * @param cityName The name of the city
     * @return Result containing WeatherInfo on success, or error on failure
     */
    suspend fun getWeatherByCityName(city: String): Result<WeatherInfo>
    /**
     * Get weather information by Coordinates
     * @param latitude, longitude The coordinates of the city
     * @return Result containing WeatherInfo on success, or error on failure
     */
    suspend fun getWeatherByCoordinates(latitude: Double, longitude: Double): Result<WeatherInfo>

    fun getAllCities(): Flow<List<City>>

    suspend fun saveCity(city: City): Result<Unit>

    suspend fun deleteCity(city: City): Result<Unit>

    /**
     * Check if a city is already saved
     * @param cityName Name of the city to check
     * @return true if city exists, false otherwise
     */
    suspend fun isCitySaved(cityName: String): Boolean

}