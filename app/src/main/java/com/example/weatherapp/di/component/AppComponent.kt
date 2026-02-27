package com.example.weatherapp.di.component

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.di.module.AppModule
import com.example.weatherapp.di.module.DatabaseModule
import com.example.weatherapp.di.module.NetworkModule
import com.example.weatherapp.di.module.RepositoryModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, DatabaseModule::class, RepositoryModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
    fun inject(application: Application)
    fun getWeatherViewModelFactory(): ViewModelProvider.Factory
}