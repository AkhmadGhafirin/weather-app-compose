package com.cascer.weatherappcompose.apiinfra

import com.cascer.weatherappcompose.api.ConnectivityException
import com.cascer.weatherappcompose.api.RemoteWeather
import com.cascer.weatherappcompose.api.RemoteWeatherInfo
import com.cascer.weatherappcompose.api.UnexpectedException
import com.cascer.weatherappcompose.api.WeatherHttpClient
import com.cascer.weatherappcompose.api.WeatherHttpClientResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class WeatherRetrofitClient @Inject constructor(
    private val service: WeatherService
) : WeatherHttpClient {
    override fun loadWeather(
        cityName: String,
        apiKey: String
    ): Flow<WeatherHttpClientResult<RemoteWeather>> = flow {
        try {
            val response = service.getWeather(cityName, apiKey).toAppLogic()
            emit(WeatherHttpClientResult.Success(response))
        } catch (exception: Exception) {
            when (exception) {
                is IOException -> {
                    emit(WeatherHttpClientResult.Failure(ConnectivityException()))
                }

                else -> {
                    emit(WeatherHttpClientResult.Failure(UnexpectedException()))
                }
            }
        }
    }

    override fun loadForecast(
        cityId: Int,
        apiKey: String
    ): Flow<WeatherHttpClientResult<List<RemoteWeatherInfo>>> = flow {
        try {
            val response = service.getForecast(cityId, apiKey).list.map { it.toAppLogic() }
            val list = response.flatMap { it.weather }
            emit(WeatherHttpClientResult.Success(list))
        } catch (exception: Exception) {
            when (exception) {
                is IOException -> {
                    emit(WeatherHttpClientResult.Failure(ConnectivityException()))
                }

                else -> {
                    emit(WeatherHttpClientResult.Failure(UnexpectedException()))
                }
            }
        }
    }
}