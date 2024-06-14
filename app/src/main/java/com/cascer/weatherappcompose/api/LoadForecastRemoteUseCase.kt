package com.cascer.weatherappcompose.api

import com.cascer.weatherappcompose.domain.Connectivity
import com.cascer.weatherappcompose.domain.LoadForecastUseCase
import com.cascer.weatherappcompose.domain.Result
import com.cascer.weatherappcompose.domain.Unexpected
import com.cascer.weatherappcompose.domain.WeatherInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoadForecastRemoteUseCase @Inject constructor(
    private val client: ForecastHttpClient
) : LoadForecastUseCase {
    override fun load(
        cityId: Int,
        apiKey: String
    ): Flow<Result<List<WeatherInfo>>> = flow {
        client.load(cityId, apiKey).collect { result ->
            when (result) {
                is HttpClientResult.Success -> {
                    emit(Result.Success(result.data.map { it.toDomain() }))
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