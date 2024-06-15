package com.cascer.weatherappcompose.api

import kotlinx.coroutines.flow.Flow

interface ForecastHttpClient {
    fun load(cityId: Int, apiKey: String): Flow<HttpClientResult>
}