package com.example.weatherapp.framework.db

import android.content.Context
import com.example.weatherapp.data.FavoriteLocationsDataSource
import com.example.weatherapp.data.model.LocationModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RoomFavoriteLocationsDataSource @Inject constructor(
    @ApplicationContext val context: Context,
    appDatabase: AppDatabase
) : FavoriteLocationsDataSource {

    private val favoriteLocationsDao = appDatabase.favoriteLocationsDao()

    override suspend fun add(location: LocationModel): Long {
        return favoriteLocationsDao.addFavoriteLocation(
            FavoriteLocationEntity(location)
        )
    }

    override suspend fun readAll(): List<LocationModel> {
        return favoriteLocationsDao.getAllFavoriteLocation().map { it.location }
    }

    override suspend fun remove(location: LocationModel): Int {
        return favoriteLocationsDao.removeFavoriteLocation(
            FavoriteLocationEntity(location)
        )
    }

    override suspend fun isStored(location: LocationModel): Boolean {
        return favoriteLocationsDao.isLocationSaved(location)
    }
}