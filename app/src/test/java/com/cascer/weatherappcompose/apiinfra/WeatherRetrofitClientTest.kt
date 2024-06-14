package com.cascer.weatherappcompose.apiinfra

import app.cash.turbine.test
import com.cascer.weatherappcompose.api.ConnectivityException
import com.cascer.weatherappcompose.api.UnexpectedException
import com.cascer.weatherappcompose.api.HttpClientResult
import com.cascer.weatherappcompose.api.remoteWeather
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException

class WeatherRetrofitClientTest {
    private val service = mockk<WeatherService>()
    private lateinit var sut: WeatherRetrofitClient

    @Before
    fun setUp() {
        sut = WeatherRetrofitClient(service)
    }

    @Test
    fun testGetFailConnectivityError() {
        expect(
            sut = sut,
            expectedResult = ConnectivityException()
        )
    }

    @Test
    fun testGetFailUnexpectedError() {
        expect(
            sut = sut,
            expectedResult = UnexpectedException()
        )
    }

    @Test
    fun testGetSuccessOn200HttpResponseWithResponse() {
        expect(
            sut = sut,
            receivedResult = weatherResponse,
            expectedResult = HttpClientResult.Success(remoteWeather)
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    private fun expect(
        sut: WeatherRetrofitClient,
        receivedResult: Any? = null,
        expectedResult: Any,
    ) = runBlocking {
        val cityName = "Davos"
        val apiKey = "123"
        when (expectedResult) {
            is ConnectivityException -> {
                coEvery {
                    service.get(cityName, apiKey)
                } throws IOException()
            }

            is HttpClientResult.Success -> {
                coEvery {
                    service.get(cityName, apiKey)
                } returns receivedResult as WeatherResponse

            }

            else -> {
                coEvery {
                    service.get(cityName, apiKey)
                } throws Exception()
            }
        }

        sut.load(cityName, apiKey).test {
            when (val result = awaitItem()) {
                is HttpClientResult.Success -> {
                    Assert.assertEquals(
                        expectedResult,
                        result
                    )
                }

                is HttpClientResult.Failure -> {
                    Assert.assertEquals(
                        expectedResult::class.java,
                        result.exception::class.java
                    )
                }
            }
            awaitComplete()
        }



        coVerify(exactly = 1) {
            service.get(cityName, apiKey)
        }

        confirmVerified(service)
    }
}