package com.example.weatherapp.data.usecases

import com.example.weatherapp.data.repositories.DeviceLocationRepository
import javax.inject.Inject

interface IsLocationEnabled {
    suspend operator fun invoke(): Boolean
}

class IsLocationEnabledImpl @Inject constructor(
    private val deviceLocationRepository: DeviceLocationRepository
) : IsLocationEnabled {
    override suspend operator fun invoke(): Boolean {
        return deviceLocationRepository.isLocationEnabled()
    }
}