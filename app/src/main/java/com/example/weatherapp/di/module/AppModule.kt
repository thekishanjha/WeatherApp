package com.example.weatherapp.di.module

import android.app.Application
import android.content.Context
import com.example.weatherapp.BuildConfig
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
class AppModule{
    @Provides
    @Singleton
    @Named("apiKey")
    fun provideApiKey(): String {
        return BuildConfig.WEATHER_API_KEY
    }

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }
}