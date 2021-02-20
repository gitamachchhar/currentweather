package com.example.currentweather.model

import com.google.gson.annotations.SerializedName

data class CurrentWeatherModel(

    @SerializedName("main")
    var main : MainWeatherObject ?= null
)