package com.cascer.weatherappcompose.domain

import kotlinx.coroutines.flow.Flow
import java.lang.Exception

sealed class LoadWeatherResult<T> {
    data class Success<T>(val data: T) : LoadWeatherResult<T>()
    data class Failure<T>(val exception: Exception) : LoadWeatherResult<T>()
}

interface LoadWeatherUseCase {
    fun loadWeather(cityName: String, apiKey: String): Flow<LoadWeatherResult<Weather>>
    fun loadForecast(cityId: Int, apiKey: String): Flow<LoadWeatherResult<List<WeatherInfo>>>
}