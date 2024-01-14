package com.example.weatherapp.data.repositories

import com.example.weatherapp.data.model.LocationSuggestion
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse

interface PlacesRepository {
    fun fetchLocationSuggestions(query: String): Task<FindAutocompletePredictionsResponse>

    fun requestLocationDetails(locationSuggestion: LocationSuggestion): Task<FetchPlaceResponse>
}