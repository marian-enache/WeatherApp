package com.example.weatherapp.data.usecases

import com.example.weatherapp.data.repositories.DeviceLocationRepository
import com.example.weatherapp.framework.PermissionResult
import javax.inject.Inject

interface GetLocationPermissionResult {
    suspend operator fun invoke(): PermissionResult
}

class GetLocationPermissionResultImpl @Inject constructor(
    private val deviceLocationRepository: DeviceLocationRepository
) : GetLocationPermissionResult {
    override suspend operator fun invoke(): PermissionResult {
        return deviceLocationRepository.requestLocationPermission()
    }
}