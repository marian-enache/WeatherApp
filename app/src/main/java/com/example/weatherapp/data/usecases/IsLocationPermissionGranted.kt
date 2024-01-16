package com.example.weatherapp.data.usecases

import com.example.weatherapp.data.repositories.DeviceLocationRepository
import javax.inject.Inject

interface IsLocationPermissionGranted {
    suspend operator fun invoke(): Boolean
}

class IsLocationPermissionGrantedImpl @Inject constructor(
    private val deviceLocationRepository: DeviceLocationRepository
) : IsLocationPermissionGranted {
    override suspend operator fun invoke(): Boolean {
        return deviceLocationRepository.isLocationPermissionGranted()
    }
}