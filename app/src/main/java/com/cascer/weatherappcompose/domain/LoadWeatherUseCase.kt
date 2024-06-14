package com.cascer.weatherappcompose.domain

import kotlinx.coroutines.flow.Flow

interface LoadWeatherUseCase {
    fun load(cityName: String, apiKey: String): Flow<Result<Weather>>
}