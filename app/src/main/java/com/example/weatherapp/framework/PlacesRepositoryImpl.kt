package com.example.weatherapp.framework

import com.example.weatherapp.data.model.LocationSuggestion
import com.example.weatherapp.data.repositories.PlacesRepository
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.*
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val placesClient: PlacesClient
): PlacesRepository {

    override fun fetchLocationSuggestions(query: String): Task<FindAutocompletePredictionsResponse> {
        val request = FindAutocompletePredictionsRequest
            .builder()
            .setQuery(query)
            .build()

        return placesClient.findAutocompletePredictions(request)
    }

    override fun requestLocationDetails(locationSuggestion: LocationSuggestion): Task<FetchPlaceResponse> {
        return placesClient.fetchPlace(
            FetchPlaceRequest.newInstance(
                locationSuggestion.placeId,
                listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
            )
        )
    }
}