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
    @SerializedName("temp") val temperature: Float,
    @SerializedName("temp_min") val minTemperature: Float,
    @SerializedName("temp_max") val maxTemperature: Float,
    @SerializedName("feels_like") val feelsLikeTemperature: Float
)