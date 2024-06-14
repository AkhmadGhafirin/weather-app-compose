package com.cascer.weatherappcompose.apiinfra

val forecastResponse = ForecastResponse(
    list = listOf(
        WeatherResponse(
            main = MainInfoResponse(
                temp = 25.0,
                feelsLike = 25.0,
                tempMin = 25.0,
                tempMax = 25.0,
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
    )
)