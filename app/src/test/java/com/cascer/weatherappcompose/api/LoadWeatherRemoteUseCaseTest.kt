package com.cascer.weatherappcompose.api

import app.cash.turbine.test
import com.cascer.weatherappcompose.domain.Connectivity
import com.cascer.weatherappcompose.domain.Result
import com.cascer.weatherappcompose.domain.Unexpected
import com.cascer.weatherappcompose.domain.weather
import io.mockk.clearAllMocks
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class LoadWeatherRemoteUseCaseTest {
    private val client = spyk<WeatherHttpClient>()
    private lateinit var sut: LoadWeatherRemoteUseCase
    private val cityName = "Davos"
    private val apiKey = "123"

    @Before
    fun setUp() {
        sut = LoadWeatherRemoteUseCase(client)
    }

    @Test
    fun testInitDoesNotRequestData() {
        verify(exactly = 0) {
            client.load(cityName, apiKey)
        }

        confirmVerified(client)
    }

    @Test
    fun testLoadRequestsData() = runBlocking {
        every {
            client.load(cityName, apiKey)
        } returns flowOf()

        sut.load(cityName, apiKey).test {
            awaitComplete()
        }

        verify(exactly = 1) {
            client.load(cityName, apiKey)
        }

        confirmVerified(client)
    }

    @Test
    fun testLoadTwiceRequestsDataTwice() = runBlocking {
        every {
            client.load(cityName, apiKey)
        } returns flowOf()

        sut.load(cityName, apiKey).test {
            awaitComplete()
        }

        sut.load(cityName, apiKey).test {
            awaitComplete()
        }

        verify(exactly = 2) {
            client.load(cityName, apiKey)
        }

        confirmVerified(client)
    }

    @Test
    fun testLoadDeliversConnectivityErrorOnClientError() {
        expect(
            sut = sut,
            receivedResult = HttpClientResult.Failure(ConnectivityException()),
            expectedResult = Connectivity()
        )
    }

    @Test
    fun testLoadDeliversUnexpectedError() {
        expect(
            sut = sut,
            receivedResult = HttpClientResult.Failure((UnexpectedException())),
            expectedResult = Unexpected()
        )
    }

    @Test
    fun testLoadDeliversWeatherOnSuccessWithWeather() {
        expect(
            sut = sut,
            receivedResult = HttpClientResult.Success(remoteWeather),
            expectedResult = Result.Success(weather)
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    private fun expect(
        sut: LoadWeatherRemoteUseCase,
        receivedResult: HttpClientResult<RemoteWeather>,
        expectedResult: Any
    ) = runBlocking {
        every {
            client.load(cityName, apiKey)
        } returns flowOf(receivedResult)

        sut.load(cityName, apiKey).test {
            when (val result = awaitItem()) {
                is HttpClientResult.Success<*> -> {
                    Assert.assertEquals(
                        expectedResult,
                        result
                    )
                }

                is HttpClientResult.Failure<*> -> {
                    Assert.assertEquals(
                        expectedResult::class.java,
                        result.exception::class.java
                    )
                }

                else -> {}
            }
            awaitComplete()
        }

        verify(exactly = 1) {
            client.load(cityName, apiKey)
        }

        confirmVerified(client)
    }
}