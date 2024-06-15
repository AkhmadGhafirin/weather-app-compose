package com.cascer.weatherappcompose.api

data class RemoteWeather(
    val main: RemoteMainInfo,
    val weather: List<RemoteWeatherInfo>,
    val name: String,
    val forecasts: List<RemoteWeatherInfo> = emptyList()
)

data class RemoteMainInfo(
    val temp: Double,
    val tempMin: Double,
    val tempMax: Double,
    val feelsLike: Double,
    val pressure: Int,
    val humidity: Int
)

data class RemoteWeatherInfo(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)