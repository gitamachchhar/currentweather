package com.example.currentweather.module

import android.app.Application
import com.example.currentweather.di.qualifiers.BaseOkHttpBuilder
import com.example.currentweather.di.scope.FeatureScope
import com.example.currentweather.local.WeatherRepository
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
    @FeatureScope
    fun getCurrentWeatherService(@BaseOkHttpBuilder retrofit: Retrofit): CurrentWeatherService {
        return retrofit.create(CurrentWeatherService::class.java)
    }


    @Provides
    @FeatureScope
    fun getNavigationViewModel(application: Application): NavigationViewModel {
        return NavigationViewModel(application)
    }

    @Provides
    @FeatureScope
    fun getCurrentWeatherViewModel(application: Application, service: CurrentWeatherService, weatherRepository: WeatherRepository): CurrentWeatherViewModel {
        return CurrentWeatherViewModel(application, service, weatherRepository)
    }

    @Provides
    @FeatureScope
    fun getLocationViewModel(application: Application): LocationViewModel {
        return LocationViewModel(application)
    }

}