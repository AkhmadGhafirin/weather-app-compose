package com.cascer.weatherappcompose.domain

import kotlinx.coroutines.flow.Flow

interface LoadForecastUseCase {
    fun load(cityId: Int, apiKey: String): Flow<Result<List<WeatherInfo>>>
}