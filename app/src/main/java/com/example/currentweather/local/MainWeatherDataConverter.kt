package com.example.currentweather.local

import android.util.Log
import androidx.room.TypeConverter
import com.example.currentweather.model.MainWeatherObject
import com.google.gson.Gson
import java.lang.reflect.Type
import com.google.gson.reflect.TypeToken

open class MainWeatherDataConverter {

    @TypeConverter
    fun stringToMainWeatherObjectItem(json: String?): MainWeatherObject? {
        try {
            val gson = Gson()
            val type: Type = object : TypeToken<MainWeatherObject?>() {}.type
            return gson.fromJson(json, type)
        } catch (e: Exception) {
            Log.e("System out", e.toString())
        }
        return MainWeatherObject()
    }

    @TypeConverter
    fun mainWeatherObjectToString(mainWeatherObject: MainWeatherObject?): String {
        try {
            val gson = Gson()
            val type: Type = object : TypeToken<MainWeatherObject?>() {}.type
            return gson.toJson(mainWeatherObject, type)
        } catch (e: Exception) {
            Log.e("System out", e.toString())
        }
        return ""
    }
}