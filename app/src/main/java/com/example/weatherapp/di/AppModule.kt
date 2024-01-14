package com.example.weatherapp.di

import android.content.Context
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.repositories.PlacesRepository
import com.example.weatherapp.data.usecases.GetLocationSuggestions
import com.example.weatherapp.data.usecases.GetLocationSuggestionsImpl
import com.example.weatherapp.framework.PlacesRepositoryImpl
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module(
    includes = [
        NetworkModule::class,
        UseCasesModule::class]
)
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    internal fun providesDispatchersProvider(dispatchersProvider: DispatchersProviderImpl): DispatchersProvider = dispatchersProvider

    @Provides
    @Singleton
    internal fun providesPlacesClient(@ApplicationContext context: Context): PlacesClient {
        if (!Places.isInitialized()) {
            Places.initialize(context, BuildConfig.PLACES_API_KEY)
        }

        return Places.createClient(context)
    }

    @Provides
    @Singleton
    fun providePlacesRepository(useCase: PlacesRepositoryImpl): PlacesRepository = useCase

}
