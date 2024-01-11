package com.example.weatherapp.di

import com.example.weatherapp.data.usecases.GetCurrentWeather
import com.example.weatherapp.data.usecases.GetCurrentWeatherImpl
import com.example.weatherapp.data.usecases.GetWeatherForecast
import com.example.weatherapp.data.usecases.GetWeatherForecastImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCasesModule {

    @Provides
    @Singleton
    fun provideGetCurrentWeather(useCase: GetCurrentWeatherImpl): GetCurrentWeather = useCase

    @Provides
    @Singleton
    fun provideGetWeatherForecast(useCase: GetWeatherForecastImpl): GetWeatherForecast = useCase

}