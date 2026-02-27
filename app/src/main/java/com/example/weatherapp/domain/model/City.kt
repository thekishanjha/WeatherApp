package com.example.weatherapp.domain.model

data class City(
    val id: Long? = null,
    val name: String,
    val latitude: Double,
    val longitude: Double
)
