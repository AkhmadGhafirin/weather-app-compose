package com.cascer.weatherappcompose.apiinfra

val weatherResponse = WeatherResponse(
    main = MainInfoResponse(
        temp = 282.7,
        feelsLike = 282.7,
        tempMin = 282.7,
        tempMax = 282.7,
        pressure = 1013,
        humidity = 83,
    ),
    weather = listOf(
        WeatherInfoResponse(
            id = 800,
            main = "Clear",
            description = "clear sky",
            icon = "01d"
        )
    ),
    name = "Davos"
)