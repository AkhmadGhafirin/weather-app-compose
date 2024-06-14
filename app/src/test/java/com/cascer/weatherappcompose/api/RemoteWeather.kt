package com.cascer.weatherappcompose.api

val remoteWeather = RemoteWeather(
    main = RemoteMainInfo(
        temp = 25.0,
        feelsLike = 25.0,
        tempMin = 25.0,
        tempMax = 25.0,
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
    name = "Davos"
)

val remoteWeatherInfo = listOf(
    RemoteWeatherInfo(
        id = 800,
        main = "Clear",
        description = "clear sky",
        icon = "01d"
    )
)