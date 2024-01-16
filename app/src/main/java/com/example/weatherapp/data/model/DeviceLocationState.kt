package com.example.weatherapp.data.model


sealed class DeviceLocationState {
    object PermissionPermanentlyDenied : DeviceLocationState()
    object PermissionDenied : DeviceLocationState()
    object LocationDisabled : DeviceLocationState()
    object LocationPermissionNeeded : DeviceLocationState()
    data class LocationAvailable(val latLang: CoordinatesModel) : DeviceLocationState()
    object Error : DeviceLocationState()
    object Loading : DeviceLocationState()
}