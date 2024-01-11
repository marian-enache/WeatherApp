package com.example.weatherapp.data.model

enum class WeatherType {
    SUNNY, CLEAR,
    CLOUDS, MIST, SMOKE, HAZE, DUST, FOG, SAND, ASH, SQUALL,
    THUNDERSTORM, DRIZZLE, RAIN, SNOW, TORNADO
}

data class WeatherModel(
    val type: WeatherType,
    val name: String,
    val currentTemp: String,
    val minTemp: String,
    val maxTemp: String
)