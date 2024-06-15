package com.cascer.weatherappcompose.core.di

import com.cascer.weatherappcompose.api.ForecastHttpClient
import com.cascer.weatherappcompose.api.WeatherHttpClient
import com.cascer.weatherappcompose.apiinfra.ForecastRetrofitClient
import com.cascer.weatherappcompose.apiinfra.ForecastService
import com.cascer.weatherappcompose.apiinfra.WeatherRetrofitClient
import com.cascer.weatherappcompose.apiinfra.WeatherService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun createMoshi(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    @Singleton
    @Provides
    fun createRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun createOkhttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun createLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    fun provideWeatherService(retrofit: Retrofit): WeatherService {
        return retrofit.create(WeatherService::class.java)
    }

    @Provides
    fun provideWeatherHttpClient(service: WeatherService): WeatherHttpClient {
        return WeatherRetrofitClient(service)
    }

    @Provides
    fun provideForecastService(retrofit: Retrofit): ForecastService {
        return retrofit.create(ForecastService::class.java)
    }

    @Provides
    fun provideForecastHttpClient(service: ForecastService): ForecastHttpClient {
        return ForecastRetrofitClient(service)
    }
}