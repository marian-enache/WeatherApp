package com.example.weatherapp.presentation

import androidx.compose.runtime.mutableStateOf
import com.example.weatherapp.presentation.model.UserCoordinates
import javax.inject.Inject

class LocationCoordinatesUIStateHolder @Inject constructor() {
    var locationEnabled = mutableStateOf(false)
    var locationPermissionPermanentlyDenied = mutableStateOf(false)
    var locationCoordinates =  mutableStateOf<UserCoordinates?>(null)
}