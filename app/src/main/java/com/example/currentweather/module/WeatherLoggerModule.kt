package com.example.currentweather.module

import android.app.Application
import com.example.currentweather.di.qualifiers.BaseOkHttpBuilder
import com.example.currentweather.remote.CurrentWeatherService
import com.example.currentweather.viewmodel.CurrentWeatherViewModel
import com.example.currentweather.viewmodel.LocationViewModel
import com.example.currentweather.viewmodel.NavigationViewModel
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit


@Module
class WeatherLoggerModule {

    @Provides
    fun getCurrentWeatherService(@BaseOkHttpBuilder retrofit: Retrofit): CurrentWeatherService {
        return retrofit.create(CurrentWeatherService::class.java)
    }


    @Provides
    fun getNavigationViewModel(application: Application): NavigationViewModel {
        return NavigationViewModel(application)
    }

    @Provides
    fun getCurrentWeatherViewModel(application: Application, service: CurrentWeatherService): CurrentWeatherViewModel {
        return CurrentWeatherViewModel(application, service)
    }

    @Provides
    fun getLocationViewModel(application: Application): LocationViewModel {
        return LocationViewModel(application)
    }

}