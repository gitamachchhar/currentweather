package com.example.currentweather.helper

import android.content.Context
import android.net.ConnectivityManager
import android.os.SystemClock
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.currentweather.helper.CurrentWeatherConstants.Companion.CURRENT_DATE_FORMAT
import java.text.SimpleDateFormat
import java.util.*


class CurrentWeatherUtils {

    companion object {

        fun isNetworkConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

        fun getCurrentFormattedDate(milliseconds: Long): String {
            val dateFormatter = SimpleDateFormat(CURRENT_DATE_FORMAT, Locale.ENGLISH)
            dateFormatter.isLenient = false
            val today = Date(milliseconds)
            return dateFormatter.format(today)
        }

        fun isDayTime() : Boolean {
            val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            return hour in 7..18
        }

        fun kelvinToCelsius(temp: Double) : String {
            return String.format(Locale.ENGLISH, "%.2f", temp - 273.15)
        }

        fun View.clickWithDebounce(debounceTime: Long = 600L, action: () -> Unit) {
            this.setOnClickListener(object : View.OnClickListener {
                private var lastClickTime: Long = 0

                override fun onClick(v: View) {
                    if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return
                    else action()
                    lastClickTime = SystemClock.elapsedRealtime()
                }
            })
        }

        fun performHapticFeedback(context: Context) {
            (context as AppCompatActivity).window.decorView.performHapticFeedback(
                HapticFeedbackConstants.VIRTUAL_KEY, HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING)
        }
    }

}
