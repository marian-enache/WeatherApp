package com.example.weatherapp.data.usecases

import com.example.weatherapp.data.mappers.LocationSuggestionMapper
import com.example.weatherapp.data.model.LocationSuggestion
import com.example.weatherapp.data.repositories.PlacesRepository
import javax.inject.Inject

interface GetLocationSuggestions {
    suspend operator fun invoke(query: String): List<LocationSuggestion>
}

class GetLocationSuggestionsImpl @Inject constructor(
    private val repository: PlacesRepository,
    private val locationSuggestionMapper: LocationSuggestionMapper
) : GetLocationSuggestions {

    override suspend operator fun invoke(query: String): List<LocationSuggestion> {
        val autocompletePredictions = repository.fetchLocationSuggestions(query)
        return locationSuggestionMapper.transform(autocompletePredictions)
    }
}