package com.example.weatherapp.data.usecases

import com.example.weatherapp.data.mappers.CurrentWeatherDataMapper
import com.example.weatherapp.data.model.CoordinatesModel
import com.example.weatherapp.data.model.WeatherModel
import com.example.weatherapp.data.repositories.WeatherRepository
import javax.inject.Inject

interface GetCurrentWeather {
    suspend operator fun invoke(coordinates: CoordinatesModel): WeatherModel?
}

class GetCurrentWeatherImpl @Inject constructor(
    private val repository: WeatherRepository,
    private val weatherMapper: CurrentWeatherDataMapper
) : GetCurrentWeather {

    override suspend operator fun invoke(coordinates: CoordinatesModel): WeatherModel? =
        repository.getCurrentWeather(coordinates.lat, coordinates.long)
            ?.let { weatherMapper.transform(it) }
}