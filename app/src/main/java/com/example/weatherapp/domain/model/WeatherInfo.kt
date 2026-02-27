package com.example.weatherapp.domain.model

data class WeatherInfo(
    val cityName: String,
    val temperature: Double,
    val feelsLike: Double = temperature,
    val description: String,
    val humidity: Int,
    val windSpeed: Double,
    val pressure: Int = 0,
    val visibility: Int = 0,
    val icon: String,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun getFormattedTemp(): String = "${temperature.toInt()}°"

    fun getWeatherEmoji(): String {
        return when {
            description.contains("clear", ignoreCase = true) -> "☀️"
            description.contains("cloud", ignoreCase = true) -> "☁️"
            description.contains("rain", ignoreCase = true) -> "🌧️"
            description.contains("snow", ignoreCase = true) -> "❄️"
            description.contains("thunder", ignoreCase = true) -> "⛈️"
            else -> "🌤️"
        }
    }
}
