package com.cascer.weatherappcompose.apiinfra

import retrofit2.http.GET
import retrofit2.http.Query

interface ForecastService {
    @GET("forecast")
    suspend fun get(
        @Query("id") cityId: Int,
        @Query("appid") apiKey: String
    ): ForecastResponse
}