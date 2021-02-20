package com.example.currentweather.module

import com.example.currentweather.di.scope.FeatureScope
import com.example.currentweather.local.WeatherDao
import com.example.currentweather.local.WeatherDao_Impl
import com.example.currentweather.local.WeatherDatabase
import dagger.Module
import dagger.Provides

@Module
class WeatherDataModule {

    @Provides
    @FeatureScope
    fun weatherDao(database: WeatherDatabase): WeatherDao {
        return WeatherDao_Impl(database)
    }
}