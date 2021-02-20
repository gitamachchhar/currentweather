package com.example.currentweather.remote

import com.example.currentweather.model.CurrentWeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrentWeatherService {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeatherData(@Query("lat") latitute: String,
                                      @Query("lon") langitude: String,
                                      @Query("appid") apiKey: String) : Response<CurrentWeatherModel>

}