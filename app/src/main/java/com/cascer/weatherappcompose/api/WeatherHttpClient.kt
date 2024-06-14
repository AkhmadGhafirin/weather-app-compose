package com.cascer.weatherappcompose.api

import kotlinx.coroutines.flow.Flow

interface WeatherHttpClient {
    fun load(cityName: String, apiKey: String): Flow<HttpClientResult>
}