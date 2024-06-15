package com.cascer.weatherappcompose.apiinfra

import app.cash.turbine.test
import com.cascer.weatherappcompose.api.ConnectivityException
import com.cascer.weatherappcompose.api.HttpClientResult
import com.cascer.weatherappcompose.api.UnexpectedException
import com.cascer.weatherappcompose.api.remoteForecastWeather
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

class ForecastRetrofitClientTest {
    private val service = mockk<ForecastService>()
    private lateinit var sut: ForecastRetrofitClient

    @Before
    fun setUp() {
        sut = ForecastRetrofitClient(service)
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
            receivedResult = forecastResponse,
            expectedResult = HttpClientResult.Success(remoteForecastWeather)
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    private fun expect(
        sut: ForecastRetrofitClient,
        receivedResult: Any? = null,
        expectedResult: Any,
    ) = runBlocking {
        val cityId = 2661039
        val apiKey = "123"
        when (expectedResult) {
            is ConnectivityException -> {
                coEvery {
                    service.get(cityId, apiKey)
                } throws IOException()
            }

            is HttpClientResult.Success -> {
                coEvery {
                    service.get(cityId, apiKey)
                } returns receivedResult as ForecastResponse
            }

            else -> {
                coEvery {
                    service.get(cityId, apiKey)
                } throws Exception()
            }
        }

        sut.load(cityId, apiKey).test {
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
            service.get(cityId, apiKey)
        }

        confirmVerified(service)
    }
}