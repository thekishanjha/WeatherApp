package com.example.weatherapp.di.module

import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.local.dao.CityDao
import com.example.weatherapp.data.remote.api.WeatherApi
import com.example.weatherapp.data.repository.impl.WeatherRepositoryImpl
import com.example.weatherapp.domain.repository.WeatherRepository
import com.example.weatherapp.domain.usecase.GetWeatherUseCase
import com.example.weatherapp.presentation.viewmodel.DaggerViewModelFactory
import com.example.weatherapp.presentation.viewmodel.WeatherViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideWeatherRepository(
        weatherApi: WeatherApi,
        cityDao: CityDao,
        @Named("apiKey") apiKey: String
    ): WeatherRepository {
        return WeatherRepositoryImpl(weatherApi, cityDao, apiKey)
    }

    @Provides
    fun provideWeatherViewModelFactory(
        getWeatherUseCase: GetWeatherUseCase
    ): ViewModelProvider.Factory {
        return DaggerViewModelFactory {
            WeatherViewModel(getWeatherUseCase)
        }
    }
}