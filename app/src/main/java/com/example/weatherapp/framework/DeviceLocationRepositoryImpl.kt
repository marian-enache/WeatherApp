package com.example.weatherapp.framework

import android.annotation.SuppressLint
import android.content.Context
import com.example.weatherapp.data.model.CoordinatesModel
import com.example.weatherapp.data.repositories.DeviceLocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Tasks
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DeviceLocationRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val locationPermissionWrapper: LocationPermissionWrapper,
    private val deviceLocationManager: DeviceLocationManager
) : DeviceLocationRepository {

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    @SuppressLint("MissingPermission")
    override suspend fun getLocationCoordinates(): CoordinatesModel {
        val task = fusedLocationClient.lastLocation

        val location = Tasks.await(task)
        return CoordinatesModel(location.latitude, location.longitude)
    }

    override suspend fun requestLocationPermission() =
        locationPermissionWrapper.requestPermission()

    override suspend fun requestLocationEnabled() =
        deviceLocationManager.requestLocationEnabled()

    override fun isLocationEnabled() =
        deviceLocationManager.isLocationEnabled()

    override fun isLocationPermissionGranted() =
        locationPermissionWrapper.hasPermission()
}