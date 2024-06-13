package com.cascer.weatherappcompose.api

import com.cascer.weatherappcompose.domain.Connectivity
import com.cascer.weatherappcompose.domain.LoadWeatherResult
import com.cascer.weatherappcompose.domain.LoadWeatherUseCase
import com.cascer.weatherappcompose.domain.Unexpected
import com.cascer.weatherappcompose.domain.Weather
import com.cascer.weatherappcompose.domain.WeatherInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoadWeatherRemoteUseCase @Inject constructor(
    private val weatherHttpClient: WeatherHttpClient
) : LoadWeatherUseCase {
    override fun loadWeather(
        cityName: String,
        apiKey: String
    ): Flow<LoadWeatherResult<Weather>> = flow {
        weatherHttpClient.loadWeather(cityName, apiKey).collect { result ->
            when (result) {
                is WeatherHttpClientResult.Success -> {
                    emit(LoadWeatherResult.Success(result.data.toDomain()))
                }

                is WeatherHttpClientResult.Failure -> {
                    when (result.exception) {
                        is ConnectivityException -> {
                            emit(LoadWeatherResult.Failure(Connectivity()))
                        }

                        is UnexpectedException -> {
                            emit(LoadWeatherResult.Failure(Unexpected()))
                        }
                    }
                }
            }
        }
    }

    override fun loadForecast(
        cityId: Int,
        apiKey: String
    ): Flow<LoadWeatherResult<List<WeatherInfo>>> = flow {
        weatherHttpClient.loadForecast(cityId, apiKey).collect { result ->
            when (result) {
                is WeatherHttpClientResult.Success -> {
                    emit(LoadWeatherResult.Success(result.data.map { it.toDomain() }))
                }

                is WeatherHttpClientResult.Failure -> {
                    when (result.exception) {
                        is ConnectivityException -> {
                            emit(LoadWeatherResult.Failure(Connectivity()))
                        }

                        is UnexpectedException -> {
                            emit(LoadWeatherResult.Failure(Unexpected()))
                        }
                    }
                }
            }
        }
    }
}