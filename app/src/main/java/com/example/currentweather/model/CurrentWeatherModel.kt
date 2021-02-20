package com.example.currentweather.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "WeatherData")
data class CurrentWeatherModel(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Int = 0,

    @ColumnInfo(name = "main")
    @SerializedName("main")
    var main : MainWeatherObject ?= null,

    @ColumnInfo(name = "date")
    @SerializedName("dt")
    var date: Long ? = 0
)