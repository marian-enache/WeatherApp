package com.example.weatherapp.presentation.model

import androidx.compose.runtime.Stable
import com.example.weatherapp.data.model.CoordinatesModel

@Stable
data class UserCoordinates(
    val lat: Double,
    val long: Double
) {
    fun toCoordinatesModel(): CoordinatesModel {
        return CoordinatesModel(lat, long)
    }
}