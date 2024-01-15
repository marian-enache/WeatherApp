package com.example.weatherapp.data.model

import com.example.weatherapp.presentation.model.UserCoordinates
import com.google.android.libraries.places.api.model.Place

data class LocationModel(
    val id: String,
    val name: String,
    val lat: Double?,
    val long: Double?,
    @Transient var isSaved: Boolean = false
) {
    companion object {
        fun from(place: Place) =
            with(place) {
                LocationModel(
                    id ?: "",
                    name ?: "",
                    latLng?.latitude,
                    latLng?.longitude
                )
            }
    }

    fun toCoordinates() = UserCoordinates(lat!!, long!!)

    fun isValid() =
        id.isNotEmpty() && name.isNotEmpty() && lat != null && long != null
}