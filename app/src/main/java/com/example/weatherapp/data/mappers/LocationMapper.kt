package com.example.weatherapp.data.mappers

import com.example.weatherapp.data.model.LocationModel
import com.google.android.libraries.places.api.model.Place
import javax.inject.Inject

class LocationMapper @Inject constructor() {
    fun transform(place: Place): LocationModel =
        with(place) {
            LocationModel(
                id ?: "",
                name ?: "",
                latLng?.latitude,
                latLng?.longitude
            )
        }
}