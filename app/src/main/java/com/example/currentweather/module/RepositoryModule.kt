package com.example.currentweather.module

import com.example.currentweather.di.scope.FeatureScope
import com.example.currentweather.local.WeatherDao
import com.example.currentweather.local.WeatherImpl
import com.example.currentweather.local.WeatherRepository
import dagger.Module
import dagger.Provides


@Module
class RepositoryModule {

    @Provides
    @FeatureScope
    fun weatherRepository(weatherDao: WeatherDao) : WeatherRepository {
        return WeatherImpl(weatherDao)
    }

}