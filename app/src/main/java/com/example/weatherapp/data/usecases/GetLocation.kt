package com.example.weatherapp.data.usecases

import com.example.weatherapp.data.mappers.LocationMapper
import com.example.weatherapp.data.model.LocationModel
import com.example.weatherapp.data.model.LocationSuggestion
import com.example.weatherapp.data.repositories.PlacesRepository
import javax.inject.Inject

interface GetLocation {
    operator fun invoke(
        suggestion: LocationSuggestion,
        onLocationFound: (LocationModel) -> Unit,
        onLocationNotFound: () -> Unit
    )
}

class GetLocationImpl @Inject constructor(
    private val repository: PlacesRepository,
    private val locationMapper: LocationMapper
) : GetLocation {

    override operator fun invoke(
        suggestion: LocationSuggestion,
        onLocationFound: (LocationModel) -> Unit,
        onLocationNotFound: () -> Unit
    ) {
        repository.requestLocationDetails(suggestion)
            .addOnSuccessListener { response ->
                onLocationFound(locationMapper.transform(response.place))
            }.addOnFailureListener {
                onLocationNotFound()
            }
    }
}