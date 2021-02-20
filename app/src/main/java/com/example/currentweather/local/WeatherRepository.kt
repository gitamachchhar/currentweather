package com.example.currentweather.local

import com.example.currentweather.model.CurrentWeatherModel

interface WeatherRepository {
    fun getWeatherData(): CurrentWeatherModel
    fun insertWeatherData(weatherObject: CurrentWeatherModel)
    fun clearWeatherData()
}