package com.example.currentweather.helper

import android.content.Context
import android.net.ConnectivityManager
import java.text.SimpleDateFormat
import java.util.*


class CurrentWeatherUtils {

    companion object {

        fun isNetworkConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

        fun getCurrentFormattedDate(): String {
            val dateFormatter = SimpleDateFormat("MMMM dd, hh:mm", Locale.ENGLISH)
            dateFormatter.isLenient = false
            val today = Date()
            return dateFormatter.format(today)
        }

        fun isDayTime() : Boolean {
            val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            return hour < 19
        }

        fun kelvinToCelsius(temp: Double) : String {
            return String.format(Locale.ENGLISH, "%.2f", temp - 273.15)
        }
    }

}
