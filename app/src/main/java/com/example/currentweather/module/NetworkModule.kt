package com.example.currentweather.module

import com.example.currentweather.BuildConfig
import com.example.currentweather.di.qualifiers.BaseOkHttpBuilder
import com.example.currentweather.remote.HeaderInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
class NetworkModule {

    @Provides
    fun getHeaderInterceptor() : HeaderInterceptor {
        return HeaderInterceptor()
    }


    @Provides
    @BaseOkHttpBuilder
    fun baseOkHttpBuilder(headerInterceptor: HeaderInterceptor): Retrofit {
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(headerInterceptor)

        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVICE_BASE_URL)
            .client(builder.build())
            .addConverterFactory(GsonConverterFactory.create()).build()
    }




}