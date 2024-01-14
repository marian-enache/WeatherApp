package com.example.weatherapp.data.mappers

import com.example.weatherapp.data.model.LocationSuggestion
import com.google.android.libraries.places.api.model.AutocompletePrediction
import javax.inject.Inject

class LocationSuggestionMapper @Inject constructor() {
    fun transform(autocompletePrediction: AutocompletePrediction): LocationSuggestion =
        LocationSuggestion(
            autocompletePrediction.getFullText(null).toString(),
            autocompletePrediction.placeId
        )

    fun transform(autocompletePredictions: List<AutocompletePrediction>): List<LocationSuggestion> =
        autocompletePredictions.map { transform(it) }
}