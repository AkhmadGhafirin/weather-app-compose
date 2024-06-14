package com.cascer.weatherappcompose.domain

val weather = Weather(
    main = MainInfo(
        temp = 25.0,
        feelsLike = 25.0,
        tempMin = 25.0,
        tempMax = 25.0,
        pressure = 1013,
        humidity = 83,
        tempMaxFormatted = "25.0°C",
        tempMinFormatted = "25.0°C",
        tempFormatted = "25.0°C",
    ),
    weather = listOf(
        WeatherInfo(
            id = 800,
            main = "Clear",
            description = "clear sky",
            icon = "01d"
        )
    ),
    forecasts = listOf(
        WeatherInfo(
            id = 800,
            main = "Clear",
            description = "clear sky",
            icon = "01d"
        ),
        WeatherInfo(
            id = 800,
            main = "Clear",
            description = "clear sky",
            icon = "01d"
        ),
        WeatherInfo(
            id = 800,
            main = "Clear",
            description = "clear sky",
            icon = "01d"
        ),
    ),
    name = "Davos"
)

val weatherInfoList = listOf(
    WeatherInfo(
        id = 800,
        main = "Clouds",
        description = "clear sky",
        icon = "01d"
    ),
    WeatherInfo(
        id = 800,
        main = "Clouds",
        description = "clear sky",
        icon = "01d"
    ),
    WeatherInfo(
        id = 800,
        main = "Clouds",
        description = "clear sky",
        icon = "01d"
    ),
)