package com.example.weatherapp.data.usecases

import com.example.weatherapp.data.mappers.LocationMapper
import com.example.weatherapp.data.model.LocationModel
import com.example.weatherapp.data.model.LocationSuggestion
import com.example.weatherapp.data.repositories.PlacesRepository
import javax.inject.Inject

interface GetLocationDetails {
    suspend operator fun invoke(suggestion: LocationSuggestion): LocationModel
}

class GetLocationDetailsImpl @Inject constructor(
    private val repository: PlacesRepository,
    private val locationMapper: LocationMapper
) : GetLocationDetails {

    override suspend operator fun invoke(suggestion: LocationSuggestion): LocationModel {
        val placeResult = repository.requestLocationDetails(suggestion)
        return locationMapper.transform(placeResult)
    }
}