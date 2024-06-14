package com.cascer.weatherappcompose.domain

val weather = Weather(
    main = MainInfo(
        temp = 282.7,
        feelsLike = 282.7,
        tempMin = 282.7,
        tempMax = 282.7,
        pressure = 1013,
        humidity = 83,
        tempMaxFormatted = "9.55°C",
        tempMinFormatted = "9.55°C",
        tempFormatted = "9.55°C",
    ),
    weather = listOf(
        WeatherInfo(
            id = 800,
            main = "Clear",
            description = "clear sky",
            icon = "01d"
        )
    ),
    forecasts = listOf(),
    name = "Davos"
)

val forecastWeather = Weather(
    main = MainInfo(
        temp = 0.0,
        feelsLike = 0.0,
        tempMin = 0.0,
        tempMax = 0.0,
        pressure = 0,
        humidity = 0,
        tempMaxFormatted = "-273.15°C",
        tempMinFormatted = "-273.15°C",
        tempFormatted = "-273.15°C",
    ),
    weather = listOf(),
    forecasts = listOf(
        WeatherInfo(
            id = 800,
            main = "Clear",
            description = "clear sky",
            icon = "01d"
        )
    ),
    name = ""
)