package com.example.weatherapp.data.mappers

import com.example.weatherapp.data.model.ForecastModel
import com.example.weatherapp.data.model.WeatherType
import com.example.weatherapp.domain.model.Forecast
import com.example.weatherapp.domain.model.ForecastResponse
import com.example.weatherapp.utils.DateUtils
import javax.inject.Inject
import kotlin.math.roundToInt

class WeatherForecastDataMapper @Inject constructor() {
    fun transform(forecast: Forecast): ForecastModel {
        val weatherList = forecast.weather
        val weatherName = weatherList.firstOrNull()?.main
        val weather = forecast.main
        return ForecastModel(
            type = weatherName?.let { WeatherType.valueOf(it.uppercase()) } ?: WeatherType.SUNNY,
            currentTemp = weather.temperature.roundToInt().toString(),
            day = DateUtils.getDayFromDate(forecast.dateTimeText) ?: ""
        )
    }

    fun transform(forecastResponse: ForecastResponse): List<ForecastModel> =
        forecastResponse.list
            .filter { DateUtils.getHourOfDay(it.dateTimeText) in 12..18 }
            .map { transform(it) }
            .distinctBy { it.day }
}