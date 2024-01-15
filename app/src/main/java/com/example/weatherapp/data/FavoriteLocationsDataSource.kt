package com.example.weatherapp.data

import com.example.weatherapp.data.model.LocationModel

interface FavoriteLocationsDataSource {
    suspend fun add(location: LocationModel): Long

    suspend fun readAll(): List<LocationModel>

    suspend fun remove(location: LocationModel): Int

    suspend fun isStored(location: LocationModel): Boolean
}