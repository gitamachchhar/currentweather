package com.example.currentweather.model

import com.google.gson.annotations.SerializedName

data class MainWeatherObject(

    @SerializedName("temp")
    var temp: Double? = null,

    @SerializedName("feels_like")
    var feelsLike: Double? = null,

    @SerializedName("humidity")
    var humidity: Int? = null,

    @SerializedName("pressure")
    var pressure: Int? = null

)