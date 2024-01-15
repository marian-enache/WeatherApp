package com.example.weatherapp.data.usecases

import com.example.weatherapp.data.FavoriteLocationsDataSource
import com.example.weatherapp.data.model.LocationModel
import javax.inject.Inject

interface GetFavoriteLocations {
    suspend operator fun invoke(): List<LocationModel>
}

class GetFavoriteLocationsImpl @Inject constructor(private val dataSource: FavoriteLocationsDataSource) :
    GetFavoriteLocations {

    override suspend operator fun invoke(): List<LocationModel> {
        return dataSource.readAll().map { it.apply { isSaved = true } }
    }
}