package com.example.weatherapp.di

import android.content.Context
import androidx.room.Room
import com.example.weatherapp.BuildConfig
import com.example.weatherapp.data.FavoriteLocationsDataSource
import com.example.weatherapp.data.repositories.DeviceLocationRepository
import com.example.weatherapp.data.repositories.PlacesRepository
import com.example.weatherapp.framework.DeviceLocationRepositoryImpl
import com.example.weatherapp.framework.PlacesRepositoryImpl
import com.example.weatherapp.framework.db.AppDatabase
import com.example.weatherapp.framework.db.RoomFavoriteLocationsDataSource
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
    fun providePlacesRepository(reository: PlacesRepositoryImpl): PlacesRepository = reository

    @Provides
    @Singleton
    fun provideDeviceLocationRepository(reository: DeviceLocationRepositoryImpl): DeviceLocationRepository = reository

    @Provides
    @Singleton
    fun provideRoomDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideFavoriteLocationsDataSource(roomFavoriteLocationsDataSource: RoomFavoriteLocationsDataSource): FavoriteLocationsDataSource =
        roomFavoriteLocationsDataSource
}
