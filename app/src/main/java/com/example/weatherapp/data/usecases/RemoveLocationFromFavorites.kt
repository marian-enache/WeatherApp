package com.example.weatherapp.data.usecases

import com.example.weatherapp.data.FavoriteLocationsDataSource
import com.example.weatherapp.data.model.LocationModel
import javax.inject.Inject

interface RemoveLocationFromFavorites {
    suspend operator fun invoke(location: LocationModel): Boolean
}

class RemoveLocationFromFavoritesImpl @Inject constructor(private val dataSource: FavoriteLocationsDataSource) :
    RemoveLocationFromFavorites {

    override suspend operator fun invoke(location: LocationModel) =
        dataSource.remove(location) > 0
}