package com.example.weatherapp.data.usecases

import com.example.weatherapp.data.mappers.WeatherForecastDataMapper
import com.example.weatherapp.data.model.CoordinatesModel
import com.example.weatherapp.data.model.ForecastModel
import com.example.weatherapp.data.repositories.WeatherRepository
import javax.inject.Inject

interface GetWeatherForecast {
    suspend operator fun invoke(coordinates: CoordinatesModel): List<ForecastModel>
}

class GetWeatherForecastImpl @Inject constructor(
    private val repository: WeatherRepository,
    private val forecastDataMapper: WeatherForecastDataMapper
) : GetWeatherForecast {

    override suspend operator fun invoke(coordinates: CoordinatesModel): List<ForecastModel> =
        repository.getNext5DayForecast(coordinates.lat, coordinates.long)
            ?.let { forecastDataMapper.transform(it) } ?: emptyList()
}