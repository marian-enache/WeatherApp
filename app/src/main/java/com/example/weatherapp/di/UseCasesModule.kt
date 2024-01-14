package com.example.weatherapp.di

import com.example.weatherapp.data.usecases.*
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

    @Provides
    @Singleton
    fun provideGetLocationSuggestions(useCase: GetLocationSuggestionsImpl): GetLocationSuggestions = useCase

    @Provides
    @Singleton
    fun provideGetLocation(useCase: GetLocationImpl): GetLocation = useCase

}