package com.cascer.weatherappcompose.api

import app.cash.turbine.test
import com.cascer.weatherappcompose.domain.Connectivity
import com.cascer.weatherappcompose.domain.Result
import com.cascer.weatherappcompose.domain.Unexpected
import com.cascer.weatherappcompose.domain.weatherInfoList
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

class LoadForecastRemoteUseCaseTest {
    private val client = spyk<ForecastHttpClient>()
    private lateinit var sut: LoadForecastRemoteUseCase
    private val cityId = 2661039
    private val apiKey = "123"

    @Before
    fun setUp() {
        sut = LoadForecastRemoteUseCase(client)
    }

    @Test
    fun testInitDoesNotRequestData() {
        verify(exactly = 0) {
            client.load(cityId, apiKey)
        }

        confirmVerified(client)
    }

    @Test
    fun testLoadRequestsData() = runBlocking {
        every {
            client.load(cityId, apiKey)
        } returns flowOf()

        sut.load(cityId, apiKey).test {
            awaitComplete()
        }

        verify(exactly = 1) {
            client.load(cityId, apiKey)
        }

        confirmVerified(client)
    }

    @Test
    fun testLoadTwiceRequestsDataTwice() = runBlocking {
        every {
            client.load(cityId, apiKey)
        } returns flowOf()

        sut.load(cityId, apiKey).test {
            awaitComplete()
        }

        sut.load(cityId, apiKey).test {
            awaitComplete()
        }

        verify(exactly = 2) {
            client.load(cityId, apiKey)
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
    fun testLoadDeliversForecastOnSuccessWithForecast() {
        expect(
            sut = sut,
            receivedResult = HttpClientResult.Success(remoteWeatherInfo),
            expectedResult = Result.Success(weatherInfoList)
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    private fun expect(
        sut: LoadForecastRemoteUseCase,
        receivedResult: HttpClientResult<List<RemoteWeatherInfo>>,
        expectedResult: Any
    ) = runBlocking {
        every {
            client.load(cityId, apiKey)
        } returns flowOf(receivedResult)

        sut.load(cityId, apiKey).test {
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
            client.load(cityId, apiKey)
        }

        confirmVerified(client)
    }
}