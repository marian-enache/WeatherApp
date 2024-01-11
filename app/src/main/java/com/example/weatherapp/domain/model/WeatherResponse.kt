package com.example.weatherapp.domain.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val weather: List<Weather>,
    val main: Main
)

data class Weather(
    val main: String,
)

data class Main(
    @SerializedName("temp") val temperature: Double,
    @SerializedName("temp_min") val minTemperature: Double,
    @SerializedName("temp_max") val maxTemperature: Double,
    @SerializedName("feels_like") val feelsLikeTemperature: Double
)