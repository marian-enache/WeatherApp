package com.example.weatherapp.data.usecases

import com.example.weatherapp.data.model.CoordinatesModel
import com.example.weatherapp.data.repositories.DeviceLocationRepository
import javax.inject.Inject

interface GetDeviceLocationCoordinates {
    suspend operator fun invoke(): CoordinatesModel
}

class GetDeviceLocationCoordinatesImpl @Inject constructor(
    private val deviceLocationRepository: DeviceLocationRepository
) : GetDeviceLocationCoordinates {
    override suspend operator fun invoke(): CoordinatesModel {
        return deviceLocationRepository.getLocationCoordinates()
    }
}