package com.cascer.weatherappcompose.api

sealed class HttpClientResult<T> {
    data class Success<T>(val data: T) : HttpClientResult<T>()
    data class Failure<T>(val exception: Exception) : HttpClientResult<T>()
}