package com.example.weatherapp.data.interactors

import com.example.weatherapp.data.model.LocationModel
import com.example.weatherapp.data.model.LocationSuggestion
import com.example.weatherapp.data.usecases.CheckLocationSavedAsFavorite
import com.example.weatherapp.data.usecases.GetLocationDetails
import javax.inject.Inject

class GetLocationAndSaveStateInteractor @Inject constructor(
    val getLocationDetails: GetLocationDetails,
    val checkLocationSavedAsFavorite: CheckLocationSavedAsFavorite
) {

    suspend operator fun invoke(suggestion: LocationSuggestion): LocationModel {
        return getLocationDetails(suggestion)
            .apply {
                val isSavedResult = checkLocationSavedAsFavorite(this)
                isSaved = isSavedResult
            }
    }
}