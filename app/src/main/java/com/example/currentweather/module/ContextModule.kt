package com.example.currentweather.module

import android.app.Application
import android.content.Context
import com.example.currentweather.di.scope.FeatureScope

import dagger.Module
import dagger.Provides

@Module
class ContextModule(val application: Application) {

    private val context: Context = application.applicationContext

    @Provides
    @FeatureScope
    fun context(): Context {
        return context
    }

    @Provides
    @FeatureScope
    internal fun provideApplication(): Application {
        return application
    }

}