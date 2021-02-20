package com.example.currentweather.module

import android.content.Context
import androidx.room.Room
import com.example.currentweather.di.scope.FeatureScope
import com.example.currentweather.helper.CurrentWeatherConstants.Companion.DATABASE_NAME
import com.example.currentweather.local.WeatherDatabase
import dagger.Module
import dagger.Provides

@Module
class RoomDatabaseModule {

    @Provides
    @FeatureScope
    fun getWeatherDatabase(context: Context): WeatherDatabase {
        return Room.databaseBuilder(context, WeatherDatabase::class.java, DATABASE_NAME).fallbackToDestructiveMigration().build()
    }
}