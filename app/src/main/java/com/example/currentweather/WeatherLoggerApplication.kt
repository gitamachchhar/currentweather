package com.example.currentweather

import com.example.currentweather.di.component.DaggerMainAppComponent
import com.example.currentweather.di.component.MainAppComponent
import com.example.currentweather.module.ContextModule
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class WeatherLoggerApplication : DaggerApplication() {


    private var mainAppComponent: MainAppComponent?= null

    override fun onCreate() {
        super.onCreate()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication>? {
        mainAppComponent = DaggerMainAppComponent.builder()
            .contextModule(ContextModule(this))
            .build()
        mainAppComponent?.inject(this)
        return mainAppComponent
    }

}