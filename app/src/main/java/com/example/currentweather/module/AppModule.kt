package com.example.currentweather.module

import android.app.Application
import dagger.Module
import dagger.Provides


@Module
class AppModule(private val mApplication: Application) {

    @Provides
    internal fun provideApplication(): Application {
        return mApplication
    }
}