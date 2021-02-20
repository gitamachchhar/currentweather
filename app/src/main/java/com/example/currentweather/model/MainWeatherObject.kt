package com.example.currentweather.model

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName

data class MainWeatherObject(

    @ColumnInfo(name = "temp")
    @SerializedName("temp")
    var temp: Double? = null,

    @ColumnInfo(name = "feels_like")
    @SerializedName("feels_like")
    var feelsLike: Double? = null,

    @SerializedName("humidity")
    var humidity: Int? = null,

    @SerializedName("pressure")
    var pressure: Int? = null

)