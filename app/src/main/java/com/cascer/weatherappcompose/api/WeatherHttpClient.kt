package com.cascer.weatherappcompose.api

import kotlinx.coroutines.flow.Flow
import java.lang.Exception

sealed class WeatherHttpClientResult<T> {
    data class Success<T>(val data: T) : WeatherHttpClientResult<T>()
    data class Failure<T>(val exception: Exception) : WeatherHttpClientResult<T>()
}

interface WeatherHttpClient {
    fun loadWeather(cityName: String, apiKey: String): Flow<WeatherHttpClientResult<RemoteWeather>>
    fun loadForecast(cityId: Int, apiKey: String): Flow<WeatherHttpClientResult<List<RemoteWeatherInfo>>>
}