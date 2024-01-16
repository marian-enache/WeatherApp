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
    fun provideGetLocation(useCase: GetLocationDetailsImpl): GetLocationDetails = useCase

    @Provides
    @Singleton
    fun provideAddLocationToFavorites(useCase: AddLocationToFavoritesImpl): AddLocationToFavorites = useCase

    @Provides
    @Singleton
    fun provideRemoveLocationFromFavorites(useCase: RemoveLocationFromFavoritesImpl): RemoveLocationFromFavorites = useCase

    @Provides
    @Singleton
    fun provideGetFavoriteLocations(useCase: GetFavoriteLocationsImpl): GetFavoriteLocations = useCase

    @Provides
    @Singleton
    fun provideCheckLocationSavedAsFavorite(useCase: CheckLocationSavedAsFavoriteImpl): CheckLocationSavedAsFavorite = useCase

    @Provides
    @Singleton
    fun provideGetDeviceLocationCoordinates(useCase: GetDeviceLocationCoordinatesImpl): GetDeviceLocationCoordinates = useCase

    @Provides
    @Singleton
    fun provideAskForLocationPermission(useCase: GetLocationPermissionResultImpl): GetLocationPermissionResult = useCase

    @Provides
    @Singleton
    fun provideAskForLocationEnable(useCase: AskForLocationEnableImpl): AskForLocationEnable = useCase

    @Provides
    @Singleton
    fun provideIsLocationEnabled(useCase: IsLocationEnabledImpl): IsLocationEnabled = useCase

    @Provides
    @Singleton
    fun provideIsLocationPermissionGranted(useCase: IsLocationPermissionGrantedImpl): IsLocationPermissionGranted = useCase
}