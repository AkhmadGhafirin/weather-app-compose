package com.cascer.weatherappcompose.api

val remoteWeather = RemoteWeather(
    main = RemoteMainInfo(
        temp = 282.7,
        feelsLike = 282.7,
        tempMin = 282.7,
        tempMax = 282.7,
        pressure = 1013,
        humidity = 83,
    ),
    weather = listOf(
        RemoteWeatherInfo(
            id = 800,
            main = "Clear",
            description = "clear sky",
            icon = "01d"
        )
    ),
    name = "Davos",
    forecasts = listOf()
)

val remoteForecastWeather = RemoteWeather(
    main = RemoteMainInfo(
        temp = 0.0,
        feelsLike = 0.0,
        tempMin = 0.0,
        tempMax = 0.0,
        pressure = 0,
        humidity = 0,
    ),
    weather = listOf(),
    name = "",
    forecasts = listOf(
        RemoteWeatherInfo(
            id = 800,
            main = "Clear",
            description = "clear sky",
            icon = "01d"
        )
    )
)