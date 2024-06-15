package com.cascer.weatherappcompose.presentation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cascer.weatherappcompose.core.di.RemoteLoadForecastUseCaseAnnotation
import com.cascer.weatherappcompose.core.di.RemoteLoadWeatherUseCaseAnnotation
import com.cascer.weatherappcompose.domain.Connectivity
import com.cascer.weatherappcompose.domain.LoadForecastUseCase
import com.cascer.weatherappcompose.domain.LoadWeatherUseCase
import com.cascer.weatherappcompose.domain.Result
import com.cascer.weatherappcompose.domain.Weather
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
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

    private val cities = listOf(
        mapOf("id" to 4899170, "name" to "Lake Zurich"),
        mapOf("id" to 6244895, "name" to "Upper Hutt"),
        mapOf("id" to 2661039, "name" to "Davos"),
        mapOf("id" to 5879092, "name" to "Alaska"),
        mapOf("id" to 5780908, "name" to "Sahara Village"),
        mapOf("id" to 5781070, "name" to "Sandy Hills"),
        mapOf("id" to 792680, "name" to "Belgrade"),
        mapOf("id" to 4350049, "name" to "California"),
    )

    private val tabs = listOf("Clear", "Clouds", "Rain", "Snow")

    private val _weatherList = mutableListOf<Weather>()

    private val viewModelState = MutableStateFlow(
        WeatherViewModelState(
            tabs = tabs,
//            isLoading = true,
            isLoading = false,
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
        load()
    }

    fun load(tab: String = "Clear") = CoroutineScope(Dispatchers.IO).launch {
        _weatherList.clear()
        viewModelState.update {
            it.copy(isLoading = true)
        }
        var index = 0
        flow {
            supervisorScope {
                repeat(8) {
                    val city = cities[index]
                    val id = city["id"] as Int
                    val name = city["name"] as String
                    val forecast = async { forecastUseCase.load(cityId = id, apiKey = API_KEY) }
                    val weather = async { weatherUseCase.load(cityName = name, apiKey = API_KEY) }
                    val result = awaitAll(forecast, weather)
                    index++
                    emit(result)
                }
            }
        }.onCompletion {
            filterByTab(tab)
        }.collect {
            val resultWeather = mutableStateOf<Weather?>(null)
            it.firstOrNull()?.collect { result -> collectData(result, resultWeather) }
            it.lastOrNull()?.collect { result -> collectData(result, resultWeather) }
        }
    }

    private fun collectData(
        result: Result,
        resultWeather: MutableState<Weather?>
    ) {
        when (result) {
            is Result.Success -> {
                if (result.data.forecasts.isNotEmpty()) {
                    resultWeather.value = result.data
                } else {
                    resultWeather.value = resultWeather.value?.copy(
                        main = result.data.main,
                        weather = result.data.weather,
                        name = result.data.name
                    )
                    _weatherList.add(resultWeather.value ?: Weather())
                }
            }

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

    private fun filterByTab(tab: String) {
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

    companion object {
        private const val API_KEY = "1b7eeecd2ff64dc83e8dcf1f4cb2102b"
        private const val CONNECTIVITY_FAIL = "Tidak ada internet"
        private const val UNEXPECTED_FAIL = "Terjadi kesalahan, coba lagi"
    }
}