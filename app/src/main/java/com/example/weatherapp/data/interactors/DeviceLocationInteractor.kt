package com.example.weatherapp.data.interactors

import com.example.weatherapp.data.model.DeviceLocationState
import com.example.weatherapp.data.model.DeviceLocationState.*
import com.example.weatherapp.data.usecases.*
import com.example.weatherapp.framework.PermissionResult
import javax.inject.Inject

class DeviceLocationInteractor @Inject constructor(
    private val getDeviceLocationCoordinates: GetDeviceLocationCoordinates,
    private val askForLocationEnable: AskForLocationEnable,
    private val getLocationPermissionResult: GetLocationPermissionResult,
    private val isLocationEnabled: IsLocationEnabled,
    private val isLocationPermissionGranted: IsLocationPermissionGranted
) {

    suspend fun getDeviceLocationState(): DeviceLocationState {
        return when {
            !isLocationEnabled() -> LocationDisabled
            !isLocationPermissionGranted() -> LocationPermissionNeeded
            else -> LocationAvailable(getDeviceLocationCoordinates())
        }
    }

    suspend fun fetchForLocationPermission(): DeviceLocationState {
        return when (getLocationPermissionResult()) {
            PermissionResult.GRANTED -> LocationAvailable(getDeviceLocationCoordinates())
            PermissionResult.DENIED -> PermissionDenied
            else -> PermissionPermanentlyDenied
        }
    }

    suspend fun goToLocationSettings() {
        askForLocationEnable()
    }
}