package com.cascer.weatherappcompose.domain

sealed class Result {
    data class Success(val data: Weather) : Result()
    data class Failure(val exception: Exception) : Result()
}