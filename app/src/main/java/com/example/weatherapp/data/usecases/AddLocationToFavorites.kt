package com.example.weatherapp.data.usecases

import com.example.weatherapp.data.FavoriteLocationsDataSource
import com.example.weatherapp.data.model.LocationModel
import javax.inject.Inject

interface AddLocationToFavorites {
    suspend operator fun invoke(location: LocationModel): Boolean
}

class AddLocationToFavoritesImpl @Inject constructor(private val dataSource: FavoriteLocationsDataSource) :
    AddLocationToFavorites {

    override suspend operator fun invoke(location: LocationModel) =
        dataSource.add(location) > 0
}