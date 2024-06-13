package com.cascer.weatherappcompose.apiinfra

import com.cascer.weatherappcompose.api.RemoteMainInfo
import com.cascer.weatherappcompose.api.RemoteWeather
import com.cascer.weatherappcompose.api.RemoteWeatherInfo

fun WeatherResponse.toAppLogic(): RemoteWeather = RemoteWeather(
    main = main.toAppLogic(),
    weather = weather.map { it.toAppLogic() },
    name = name.orEmpty()
)

fun MainInfoResponse.toAppLogic(): RemoteMainInfo = RemoteMainInfo(
    temp = temp,
    feelsLike = feelsLike,
    tempMin = tempMin,
    tempMax = tempMax,
    pressure = pressure,
    humidity = humidity
)

fun WeatherInfoResponse.toAppLogic(): RemoteWeatherInfo = RemoteWeatherInfo(
    id = id,
    main = main,
    description = description,
    icon = icon
)