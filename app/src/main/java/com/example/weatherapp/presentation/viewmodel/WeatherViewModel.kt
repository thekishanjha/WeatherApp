package com.example.weatherapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.domain.model.WeatherInfo
import com.example.weatherapp.domain.usecase.GetWeatherUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState
    private val _searchQuery = MutableStateFlow("Moscow")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var lastSearchedCity = "Moscow"


    fun getWeather(cityName: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            getWeatherUseCase(cityName).fold(
                onSuccess = { weatherInfo ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        weatherInfo = weatherInfo
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = exception.message
                    )
                }
            )
        }
    }

    //on typing in searchbar text cahnge
    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    //Search weather
    fun searchWeather(){
        val query = _searchQuery.value.trim()
        if(query.isNotEmpty()){
         lastSearchedCity = query
         getWeather(query)
        }
    }

    //Pull to Refresh Weather
    fun refreshWeather(){
        getWeather(lastSearchedCity)
    }
}

data class WeatherUiState(
    val isLoading: Boolean = false,
    val weatherInfo: WeatherInfo? = null,
    val error: String? = null
)