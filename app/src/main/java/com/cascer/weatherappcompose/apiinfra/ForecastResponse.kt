package com.cascer.weatherappcompose.apiinfra

import com.squareup.moshi.Json

data class ForecastResponse(
    @Json(name = "list")
    val list: List<WeatherResponse>
)