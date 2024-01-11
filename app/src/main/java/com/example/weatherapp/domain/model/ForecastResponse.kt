package com.example.weatherapp.domain.model

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    val list: List<Forecast>,
    val city: City
)

data class Forecast(
    val main: Main,
    val weather: List<Weather>,
    @SerializedName("dt") val date: Long,
    @SerializedName("dt_txt") val dateTimeText: String
)

data class City(
    val name: String,
)