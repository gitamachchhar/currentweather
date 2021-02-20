package com.example.currentweather.remote

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val oldRequestBody = chain.request()
        val newRequest = oldRequestBody.newBuilder().build()

        var response: Response? = null
        try {
            response = chain.proceed(newRequest)

        } catch (e: Throwable) {
            Log.e("System Out", e.message)
        }
        return response!!
    }

}