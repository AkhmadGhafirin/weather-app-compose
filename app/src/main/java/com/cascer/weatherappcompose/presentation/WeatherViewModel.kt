package com.cascer.weatherappcompose.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cascer.weatherappcompose.core.di.RemoteLoadForecastUseCaseAnnotation
import com.cascer.weatherappcompose.core.di.RemoteLoadWeatherUseCaseAnnotation
import com.cascer.weatherappcompose.domain.Connectivity
import com.cascer.weatherappcompose.domain.LoadForecastUseCase
import com.cascer.weatherappcompose.domain.LoadWeatherUseCase
import com.cascer.weatherappcompose.domain.MainInfo
import com.cascer.weatherappcompose.domain.Result
import com.cascer.weatherappcompose.domain.Weather
import com.cascer.weatherappcompose.domain.WeatherInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface WeatherUiState {
    val tabs: List<String>
    val isLoading: Boolean
    val failed: String

    data class HasWeather(
        override val tabs: List<String>,
        override val isLoading: Boolean,
        val weatherList: List<Weather>,
        override val failed: String
    ) : WeatherUiState

    data class NoWeather(
        override val tabs: List<String>,
        override val isLoading: Boolean,
        override val failed: String,
    ) : WeatherUiState
}

data class WeatherViewModelState(
    val tabs: List<String>,
    val isLoading: Boolean = false,
    val weatherList: List<Weather> = emptyList(),
    val failed: String = ""
) {
    fun toWeatherUiState(): WeatherUiState =
        if (weatherList.isEmpty()) {
            WeatherUiState.NoWeather(
                tabs = tabs,
                isLoading = isLoading,
                failed = failed
            )

        } else {
            WeatherUiState.HasWeather(
                tabs = tabs,
                isLoading = isLoading,
                weatherList = weatherList,
                failed = failed
            )
        }
}

@HiltViewModel
class WeatherViewModel @Inject constructor(
    @RemoteLoadWeatherUseCaseAnnotation private val weatherUseCase: LoadWeatherUseCase,
    @RemoteLoadForecastUseCaseAnnotation private val forecastUseCase: LoadForecastUseCase
) : ViewModel() {
    private val cities = mapOf(
        4899170 to "Lake Zurich",
        6244895 to "Upper Hutt",
        2661039 to "Davos",
        5879092 to "Alaska",
        5780908 to "Sahara Village",
        5781070 to "Sandy Hills",
        792680 to "Belgrade",
        4350049 to "California"
    )

    private val tabs = listOf("Clear", "Clouds", "Rain", "Snow")

    private val _weatherList = mutableListOf<Weather>()

    private val viewModelState = MutableStateFlow(
        WeatherViewModelState(
            tabs = tabs,
            isLoading = true,
            failed = ""
        )
    )

    val weatherUiState = viewModelState
        .map(WeatherViewModelState::toWeatherUiState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = viewModelState.value.toWeatherUiState()
        )

    init {
//        load()
    }

    private fun load(tab: String = "Clear") = viewModelScope.launch {
        cities.keys.forEach { id ->
            val resultForecast = mutableListOf<WeatherInfo>()
            val resultWeather = mutableStateOf<Weather?>(null)
            forecastUseCase.load(cityId = id, apiKey = API_KEY).collect { result ->
                when (result) {
                    is Result.Success -> resultForecast.addAll(result.data)
                    is Result.Failure -> {
                        when (result.exception) {
                            is Connectivity -> {
                                viewModelState.update { it.copy(failed = CONNECTIVITY_FAIL) }
                            }

                            else -> {
                                viewModelState.update { it.copy(failed = UNEXPECTED_FAIL) }
                            }
                        }
                    }
                }
            }

            weatherUseCase.load(cityName = cities[id].orEmpty(), apiKey = API_KEY)
                .collect { result ->
                    when (result) {
                        is Result.Success -> resultWeather.value = result.data
                        is Result.Failure -> {
                            when (result.exception) {
                                is Connectivity -> {
                                    viewModelState.update { it.copy(failed = CONNECTIVITY_FAIL) }
                                }

                                else -> {
                                    viewModelState.update { it.copy(failed = UNEXPECTED_FAIL) }
                                }
                            }
                        }
                    }
                }

            _weatherList.add(
                resultWeather.value?.copy(forecasts = resultForecast) ?: emptyWeather()
            )
        }

        filterByTab(tab)
    }

    fun filterByTab(tab: String) {
        viewModelState.update { root ->
            root.copy(
                isLoading = false,
                weatherList = _weatherList.map {
                    it.copy(forecasts = it.forecasts.filter { weatherInfo -> weatherInfo.main == tab })
                }.sortedByDescending { forecast ->
                    forecast.forecasts.filter { weatherInfo -> weatherInfo.main == tab }.size
                }
            )
        }
    }

    private fun emptyWeather() = Weather(
        main = MainInfo(0.0, 0.0, 0.0, 0.0, "", "", "", 0, 0),
        weather = emptyList(),
        forecasts = emptyList(),
        name = ""
    )

    companion object {
        private const val API_KEY = "1b7eeecd2ff64dc83e8dcf1f4cb2102b"
        private const val CONNECTIVITY_FAIL = "Tidak ada internet"
        private const val UNEXPECTED_FAIL = "Terjadi kesalahan, coba lagi"
    }
}