package com.cascer.weatherappcompose.apiinfra

import com.cascer.weatherappcompose.api.ConnectivityException
import com.cascer.weatherappcompose.api.ForecastHttpClient
import com.cascer.weatherappcompose.api.RemoteWeatherInfo
import com.cascer.weatherappcompose.api.UnexpectedException
import com.cascer.weatherappcompose.api.HttpClientResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class ForecastRetrofitClient @Inject constructor(
    private val service: ForecastService
) : ForecastHttpClient {
    override fun load(
        cityId: Int,
        apiKey: String
    ): Flow<HttpClientResult<List<RemoteWeatherInfo>>> = flow {
        try {
            val response = service.get(cityId, apiKey).list.map { it.toAppLogic() }
            val list = response.flatMap { it.weather }
            emit(HttpClientResult.Success(list))
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