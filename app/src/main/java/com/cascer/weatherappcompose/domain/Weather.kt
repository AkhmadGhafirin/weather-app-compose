package com.cascer.weatherappcompose.domain

data class Weather(
    val main: MainInfo = MainInfo(0.0, 0.0, 0.0, 0.0, "", "", "", 0, 0),
    val weather: List<WeatherInfo> = emptyList(),
    val name: String = "",
    val forecasts: List<WeatherInfo> = emptyList()
)

data class MainInfo(
    val temp: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val tempFormatted: String,
    val tempMinFormatted: String,
    val tempMaxFormatted: String,
    val pressure: Int,
    val humidity: Int
)

data class WeatherInfo(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)