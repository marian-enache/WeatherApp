package com.example.weatherapp.framework

import com.example.weatherapp.data.model.LocationSuggestion
import com.example.weatherapp.data.repositories.PlacesRepository
import com.example.weatherapp.di.DispatchersProvider
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PlacesRepositoryImpl @Inject constructor(
    private val placesClient: PlacesClient,
    private val dispatchersProvider: DispatchersProvider
): PlacesRepository {

    override suspend fun fetchLocationSuggestions(query: String): List<AutocompletePrediction> {
        val request = FindAutocompletePredictionsRequest
            .builder()
            .setQuery(query)
            .build()

        val task = placesClient.findAutocompletePredictions(request)

        return withContext(dispatchersProvider.io) {
            Tasks.await(task).autocompletePredictions
        }
    }

    override suspend fun requestLocationDetails(locationSuggestion: LocationSuggestion): Place {
        val task = placesClient.fetchPlace(
            FetchPlaceRequest.newInstance(
                locationSuggestion.placeId,
                listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG)
            )
        )

        return withContext(dispatchersProvider.io) {
            Tasks.await(task).place
        }
    }
}