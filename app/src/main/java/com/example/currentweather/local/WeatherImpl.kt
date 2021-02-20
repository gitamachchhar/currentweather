package com.example.currentweather.local

import com.example.currentweather.model.CurrentWeatherModel

class WeatherImpl (private val weatherDao: WeatherDao) : WeatherRepository {

    override fun getWeatherData(): CurrentWeatherModel {
        return weatherDao.getWeatherData
    }

    override fun insertWeatherData(weatherObject: CurrentWeatherModel) {
        return weatherDao.insertWeatherData(weatherObject)
    }

    override fun clearWeatherData() {
        return weatherDao.clearWeatherDataTable()
    }

}