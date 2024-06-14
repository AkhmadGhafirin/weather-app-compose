package com.cascer.weatherappcompose.persentation

import com.cascer.weatherappcompose.domain.LoadForecastUseCase
import com.cascer.weatherappcompose.domain.LoadWeatherUseCase
import com.cascer.weatherappcompose.presentation.WeatherUiState
import com.cascer.weatherappcompose.presentation.WeatherViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class WeatherViewModelTest {
    private val weatherUseCase = spyk<LoadWeatherUseCase>()
    private val forecastUseCase = spyk<LoadForecastUseCase>()
    private lateinit var sut: WeatherViewModel
    private val cityName = "Davos"
    private val cityId = 2661039
    private val apiKey = "1b7eeecd2ff64dc83e8dcf1f4cb2102b"

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        sut = WeatherViewModel(weatherUseCase, forecastUseCase)

        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun testInitInitialState() {
        val uiState = sut.weatherUiState.value
        val tabs = listOf("Clear", "Clouds", "Rain", "Snow")

        if (uiState is WeatherUiState.HasWeather) {
            Assert.assertTrue(uiState.isLoading)
            Assert.assertTrue(uiState.weatherList.isEmpty())
            Assert.assertEquals(tabs, uiState.tabs)
            assert(uiState.failed.isEmpty())
        } else {
            Assert.assertTrue(uiState.isLoading)
            Assert.assertEquals(tabs, uiState.tabs)
            assert(uiState.failed.isEmpty())
        }
    }

    @Test
    fun testInitWeatherDoesLoad() = runBlocking {
        coEvery {
            forecastUseCase.load(cityId, apiKey)
            weatherUseCase.load(cityName, apiKey)
        } returns flowOf()
//        coEvery {
//            weatherUseCase.load(cityName, apiKey)
//        } returns flowOf()

        sut.load()

        coVerify(exactly = 1) {
            forecastUseCase.load(cityId, apiKey)
            weatherUseCase.load(cityName, apiKey)
        }

        confirmVerified(forecastUseCase)
        confirmVerified(weatherUseCase)
    }

    @Test
    fun testInitForecastDoesLoad() {
        verify(exactly = 1) {
            forecastUseCase.load(0, "")
        }

        confirmVerified(forecastUseCase)
    }

//    @After
//    fun tearDown() {
//        clearAllMocks()
//    }
}