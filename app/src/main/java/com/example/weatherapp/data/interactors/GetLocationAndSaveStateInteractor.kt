package com.example.weatherapp.data.interactors

import com.example.weatherapp.data.model.LocationModel
import com.example.weatherapp.data.model.LocationSuggestion
import com.example.weatherapp.data.usecases.CheckLocationSavedAsFavorite
import com.example.weatherapp.data.usecases.GetLocation
import javax.inject.Inject

class GetLocationAndSaveStateInteractor @Inject constructor(
    val getLocation: GetLocation,
    val checkLocationSavedAsFavorite: CheckLocationSavedAsFavorite
) {

    suspend operator fun invoke(suggestion: LocationSuggestion): LocationModel {
        return getLocation(suggestion)
            .apply {
                val isSavedResult = checkLocationSavedAsFavorite(this)
                isSaved = isSavedResult
            }
    }
}