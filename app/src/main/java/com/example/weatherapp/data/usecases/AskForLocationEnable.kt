package com.example.weatherapp.data.usecases

import com.example.weatherapp.data.repositories.DeviceLocationRepository
import javax.inject.Inject

interface AskForLocationEnable {
    suspend operator fun invoke(): Boolean
}

class AskForLocationEnableImpl @Inject constructor(
    private val deviceLocationRepository: DeviceLocationRepository
) : AskForLocationEnable {
    override suspend operator fun invoke(): Boolean {
        return deviceLocationRepository.requestLocationEnabled()
    }
}