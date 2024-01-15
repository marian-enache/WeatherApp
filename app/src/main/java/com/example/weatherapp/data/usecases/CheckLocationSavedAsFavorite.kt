package com.example.weatherapp.data.usecases

import com.example.weatherapp.data.FavoriteLocationsDataSource
import com.example.weatherapp.data.model.LocationModel
import javax.inject.Inject

interface CheckLocationSavedAsFavorite {
    suspend operator fun invoke(location: LocationModel): Boolean
}

class CheckLocationSavedAsFavoriteImpl @Inject constructor(private val dataSource: FavoriteLocationsDataSource) :
    CheckLocationSavedAsFavorite {

    override suspend operator fun invoke(location: LocationModel) =
        dataSource.isStored(location)
}