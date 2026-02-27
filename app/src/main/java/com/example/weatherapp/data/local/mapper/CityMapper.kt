package com.example.weatherapp.data.local.mapper

import com.example.weatherapp.data.local.entity.CityEntity
import com.example.weatherapp.domain.model.City

//saving to db
fun City.toCityEntity(): CityEntity {
    return CityEntity(
        id = this.id ?: 0L,
        name = name,
        latitude = latitude,
        longitude = longitude
    )
}

//reading from db
fun CityEntity.toCity(): City {
    return City(
        id = if (this.id == 0L) null else this.id,
        name = this.name,
        latitude = this.latitude,
        longitude = this.longitude
    )
}
