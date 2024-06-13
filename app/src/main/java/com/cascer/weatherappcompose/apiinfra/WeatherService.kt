package com.cascer.weatherappcompose.apiinfra

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String
    ): WeatherResponse

    @GET("forecast")
    suspend fun getForecast(
        @Query("id") cityId: Int,
        @Query("appid") apiKey: String
    ): ForecastResponse
}