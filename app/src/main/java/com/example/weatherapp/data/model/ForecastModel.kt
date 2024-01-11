package com.example.weatherapp.data.model

data class ForecastModel(
    val type: WeatherType,
    val currentTemp: String,
    val day: String,
)