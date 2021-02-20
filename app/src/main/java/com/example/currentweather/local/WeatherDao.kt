package com.example.currentweather.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.currentweather.model.CurrentWeatherModel

@Dao
abstract class WeatherDao {

    @get:Query("SELECT * FROM WeatherData")
    abstract val getWeatherData: CurrentWeatherModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertWeatherData(weatherData: CurrentWeatherModel)

    @Query("DELETE FROM WeatherData")
    abstract fun clearWeatherDataTable()

}