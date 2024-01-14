package com.example.weatherapp.data.usecases

import com.example.weatherapp.data.mappers.LocationSuggestionMapper
import com.example.weatherapp.data.model.LocationSuggestion
import com.example.weatherapp.data.repositories.PlacesRepository
import javax.inject.Inject

interface GetLocationSuggestions {
    operator fun invoke(
        query: String,
        onSuggestionsFound: (List<LocationSuggestion>) -> Unit,
        onSuggestionsNotFound: () -> Unit
    )
}

class GetLocationSuggestionsImpl @Inject constructor(
    private val repository: PlacesRepository,
    private val locationSuggestionMapper: LocationSuggestionMapper
) : GetLocationSuggestions {

    override operator fun invoke(
        query: String,
        onSuggestionsFound: (List<LocationSuggestion>) -> Unit,
        onSuggestionsNotFound: () -> Unit
    ) {
        repository.fetchLocationSuggestions(query)
            .addOnSuccessListener { response ->
                onSuggestionsFound(locationSuggestionMapper.transform(response.autocompletePredictions))
            }.addOnFailureListener {
                onSuggestionsNotFound()
            }
    }
}