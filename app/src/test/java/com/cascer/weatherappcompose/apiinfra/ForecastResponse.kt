package com.cascer.weatherappcompose.apiinfra

val forecastResponse = ForecastResponse(
    list = listOf(
        WeatherResponse(
            main = MainInfoResponse(
                temp = 0.0,
                feelsLike = 0.0,
                tempMin = 0.0,
                tempMax = 0.0,
                pressure = 0,
                humidity = 0,
            ),
            weather = listOf(
                WeatherInfoResponse(
                    id = 800,
                    main = "Clear",
                    description = "clear sky",
                    icon = "01d"
                )
            ),
            name = "",
        )
    )
)