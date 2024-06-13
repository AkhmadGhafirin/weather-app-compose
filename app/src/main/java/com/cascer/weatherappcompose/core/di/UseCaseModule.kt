package com.cascer.weatherappcompose.core.di

import com.cascer.weatherappcompose.api.LoadWeatherRemoteUseCase
import com.cascer.weatherappcompose.api.WeatherHttpClient
import com.cascer.weatherappcompose.domain.LoadWeatherUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideLoadWeatherUseCase(client: WeatherHttpClient): LoadWeatherUseCase {
        return LoadWeatherRemoteUseCase(client)
    }
}