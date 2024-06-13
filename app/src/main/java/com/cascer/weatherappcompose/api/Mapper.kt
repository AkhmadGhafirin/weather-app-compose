package com.cascer.weatherappcompose.api

import com.cascer.weatherappcompose.domain.MainInfo
import com.cascer.weatherappcompose.domain.Weather
import com.cascer.weatherappcompose.domain.WeatherInfo
import java.text.DecimalFormat

fun RemoteWeather.toDomain(): Weather = Weather(
    name = name,
    main = main.toDomain(),
    weather = weather.map { it.toDomain() },
    forecasts = emptyList()
)

fun RemoteMainInfo.toDomain(): MainInfo = MainInfo(
    temp = temp,
    feelsLike = feelsLike,
    tempMin = tempMin,
    tempMax = tempMax,
    tempFormatted = temp.kelvinToCelsius(),
    tempMinFormatted = tempMin.kelvinToCelsius(),
    tempMaxFormatted = tempMax.kelvinToCelsius(),
    pressure = pressure,
    humidity = humidity
)

fun RemoteWeatherInfo.toDomain(): WeatherInfo = WeatherInfo(
    id = id,
    main = main,
    description = description,
    icon = icon
)

fun Double.kelvinToCelsius(): String {
    val celsius = this - 273.15
    val df = DecimalFormat("#.##")
    return "${df.format(celsius)}°C"
}