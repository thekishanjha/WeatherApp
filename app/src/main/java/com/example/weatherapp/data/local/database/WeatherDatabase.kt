package com.example.weatherapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapp.data.local.dao.CityDao
import com.example.weatherapp.data.local.entity.CityEntity

@Database(entities = [CityEntity::class], version = 1, exportSchema = false)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun cityDao(): CityDao
}