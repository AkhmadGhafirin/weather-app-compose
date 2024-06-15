package com.cascer.weatherappcompose.api

sealed class HttpClientResult {
    data class Success(val data: RemoteWeather) : HttpClientResult()
    data class Failure(val exception: Exception) : HttpClientResult()
}