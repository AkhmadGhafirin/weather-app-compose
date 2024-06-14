package com.cascer.weatherappcompose.apiinfra

import com.cascer.weatherappcompose.api.ConnectivityException
import com.cascer.weatherappcompose.api.HttpClientResult
import com.cascer.weatherappcompose.api.UnexpectedException
import com.cascer.weatherappcompose.api.WeatherHttpClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class WeatherRetrofitClient @Inject constructor(
    private val service: WeatherService
) : WeatherHttpClient {
    override fun load(
        cityName: String,
        apiKey: String
    ): Flow<HttpClientResult> = flow {
        try {
            val response = service.get(cityName, apiKey).toAppLogic()
            emit(HttpClientResult.Success(response))
        } catch (exception: Exception) {
            when (exception) {
                is IOException -> {
                    emit(HttpClientResult.Failure(ConnectivityException()))
                }

                else -> {
                    emit(HttpClientResult.Failure(UnexpectedException()))
                }
            }
        }
    }
}