package com.cascer.weatherappcompose.apiinfra

import com.squareup.moshi.Json

data class WeatherResponse(
    @Json(name = "main")
    val main: MainInfoResponse,
    @Json(name = "weather")
    val weather: List<WeatherInfoResponse>,
    @Json(name = "name")
    val name: String?
)

data class MainInfoResponse(
    @Json(name = "temp")
    val temp: Double,
    @Json(name = "feels_like")
    val feelsLike: Double,
    @Json(name = "temp_min")
    val tempMin: Double,
    @Json(name = "temp_max")
    val tempMax: Double,
    @Json(name = "pressure")
    val pressure: Int,
    @Json(name = "humidity")
    val humidity: Int
)

data class WeatherInfoResponse(
    @Json(name = "id")
    val id: Int,
    @Json(name = "main")
    val main: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "icon")
    val icon: String
)