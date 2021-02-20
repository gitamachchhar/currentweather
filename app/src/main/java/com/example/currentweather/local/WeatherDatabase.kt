package com.example.currentweather.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.currentweather.helper.CurrentWeatherConstants.Companion.DATABASE_NAME
import com.example.currentweather.model.CurrentWeatherModel


@Database(entities = [CurrentWeatherModel::class], version = 1, exportSchema = false)
@TypeConverters(MainWeatherDataConverter::class)

abstract class WeatherDatabase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao

    companion object {

        @Volatile
        private var instance: WeatherDatabase? = null

        fun getInstance(context: Context): WeatherDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): WeatherDatabase {
            return Room.databaseBuilder(context, WeatherDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                }).fallbackToDestructiveMigration()
                .build()
        }

    }

}