package com.example.weatherapp.data.repositories

import com.example.weatherapp.data.model.CoordinatesModel
import com.example.weatherapp.framework.PermissionResult

interface DeviceLocationRepository {
    suspend fun getLocationCoordinates(): CoordinatesModel

    suspend fun requestLocationPermission(): PermissionResult

    suspend fun requestLocationEnabled(): Boolean

    fun isLocationEnabled(): Boolean

    fun isLocationPermissionGranted(): Boolean
}