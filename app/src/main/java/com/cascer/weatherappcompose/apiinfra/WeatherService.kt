package com.cascer.weatherappcompose.apiinfra

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather")
    suspend fun get(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String
    ): WeatherResponse


}