package com.cascer.weatherappcompose.api

import com.cascer.weatherappcompose.domain.Connectivity
import com.cascer.weatherappcompose.domain.LoadWeatherUseCase
import com.cascer.weatherappcompose.domain.Result
import com.cascer.weatherappcompose.domain.Unexpected
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoadWeatherRemoteUseCase @Inject constructor(
    private val client: WeatherHttpClient
) : LoadWeatherUseCase {
    override fun load(
        cityName: String,
        apiKey: String
    ): Flow<Result> = flow {
        client.load(cityName, apiKey).collect { result ->
            when (result) {
                is HttpClientResult.Success -> {
                    emit(Result.Success(result.data.toDomain()))
                }

                is HttpClientResult.Failure -> {
                    when (result.exception) {
                        is ConnectivityException -> {
                            emit(Result.Failure(Connectivity()))
                        }

                        is UnexpectedException -> {
                            emit(Result.Failure(Unexpected()))
                        }
                    }
                }
            }
        }
    }
}