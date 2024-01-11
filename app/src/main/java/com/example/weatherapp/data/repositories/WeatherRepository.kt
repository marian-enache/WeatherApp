package com.example.weatherapp.data.repositories

import com.example.weatherapp.data.Api
import com.example.weatherapp.domain.model.ForecastResponse
import com.example.weatherapp.domain.model.WeatherResponse
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: Api
) {

    suspend fun getCurrentWeather(lat: Double, long: Double): WeatherResponse? {
        val response = api.getCurrentWeather(lat, long)
        if (response.isSuccessful) {
            response.body()?.let {
                return it
            }
        }
        return null
    }

    suspend fun getNext5DayForecast(lat: Double, long: Double): ForecastResponse? {
        val response = api.getForecast(lat, long)
        if (response.isSuccessful) {
            response.body()?.let {
                return it
            }
        }
        return null
    }
}