package com.example.weatherapp.data.mappers

import com.example.weatherapp.data.model.WeatherModel
import com.example.weatherapp.data.model.WeatherType
import com.example.weatherapp.domain.model.WeatherResponse
import javax.inject.Inject
import kotlin.math.roundToInt

class CurrentWeatherDataMapper @Inject constructor() {
    fun transform(weatherResponse: WeatherResponse): WeatherModel {
        val weatherName = weatherResponse.weather.firstOrNull()?.main
        val weather = weatherResponse.main
        return WeatherModel(
            type = weatherName?.let { WeatherType.valueOf(it.uppercase()) } ?: WeatherType.SUNNY,
            name = weatherName.orEmpty(),
            currentTemp = weather.temperature.roundToInt().toString(),
            minTemp = weather.minTemperature.roundToInt().toString(),
            maxTemp = weather.maxTemperature.roundToInt().toString(),
        )
    }

    fun transform(weatherList: List<WeatherResponse>): List<WeatherModel> =
        weatherList.map {
            transform(it)
        }
}