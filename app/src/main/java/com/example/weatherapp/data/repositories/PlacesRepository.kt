package com.example.weatherapp.data.repositories

import com.example.weatherapp.data.model.LocationSuggestion
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse

interface PlacesRepository {
    suspend fun fetchLocationSuggestions(query: String): List<AutocompletePrediction>

    suspend fun requestLocationDetails(locationSuggestion: LocationSuggestion): Place
}