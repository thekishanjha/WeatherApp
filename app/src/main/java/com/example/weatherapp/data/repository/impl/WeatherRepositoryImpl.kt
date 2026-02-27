package com.example.weatherapp.data.repository.impl

import com.example.weatherapp.data.local.dao.CityDao
import com.example.weatherapp.data.local.mapper.toCity
import com.example.weatherapp.data.local.mapper.toCityEntity
import com.example.weatherapp.data.remote.api.WeatherApi
import com.example.weatherapp.data.remote.mapper.toWeatherInfo
import com.example.weatherapp.domain.model.City
import com.example.weatherapp.domain.model.WeatherInfo
import com.example.weatherapp.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherApi: WeatherApi,
    private val cityDao: CityDao,
    private val apiKey: String
) : WeatherRepository {
    override suspend fun getWeatherByCityName(city: String): Result<WeatherInfo> {
        return try {
            val response = weatherApi.getWeatherByCityName(city, apiKey)
            Result.success(response.toWeatherInfo())
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun getWeatherByCoordinates(
        latitude: Double,
        longitude: Double
    ): Result<WeatherInfo> {
        return try {
            val response = weatherApi.getWeatherByCoordinates(latitude, longitude, apiKey)
            Result.success(response.toWeatherInfo())
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override fun getAllCities(): Flow<List<City>> {
        return cityDao.getAllCities().map {
            it.map { cityEntity ->
                cityEntity.toCity()
            }
        }
    }

    override suspend fun saveCity(city: City): Result<Unit> {
        return try {
            cityDao.insertCity(city.toCityEntity())
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun deleteCity(city: City): Result<Unit> {
        return try {
            val entity = city.toCityEntity()
            cityDao.deleteCity(entity)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isCitySaved(cityName: String): Boolean {
        return cityDao.isCityExists(cityName)
    }

}