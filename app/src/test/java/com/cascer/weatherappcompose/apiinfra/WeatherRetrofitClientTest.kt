package com.cascer.weatherappcompose.apiinfra

import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before

class WeatherRetrofitClientTest {
    private val service = mockk<WeatherService>()
    private lateinit var sut: WeatherRetrofitClient

    @Before
    fun setUp() {
        sut = WeatherRetrofitClient(service)
    }

    private fun expect(
        sut: WeatherRetrofitClient,
        receivedResult: Any? = null,
        expectedResult: Any
    ) = runBlocking {

    }
}