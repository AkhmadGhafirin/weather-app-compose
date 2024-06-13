package com.cascer.weatherappcompose.domain

val weatherData = listOf(
    Weather(
        main = MainInfo(
            temp = 25.0,
            feelsLike = 25.0,
            tempMin = 25.0,
            tempMax = 25.0,
            pressure = 1013,
            humidity = 83
        ),
        weather = listOf(
            WeatherInfo(
                id = 800,
                main = "Clear",
                description = "clear sky",
                icon = "01d"
            )
        ),
        name = "London"
    ),
    Weather(
        main = MainInfo(
            temp = 25.0,
            feelsLike = 25.0,
            tempMin = 25.0,
            tempMax = 25.0,
            pressure = 1013,
            humidity = 83
        ),
        weather = listOf(
            WeatherInfo(
                id = 800,
                main = "Clear",
                description = "clear sky",
                icon = "01d"
            )
        ),
        name = "Paris"
    )
)