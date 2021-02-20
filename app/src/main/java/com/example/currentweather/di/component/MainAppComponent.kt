package com.example.currentweather.di.component

import com.example.currentweather.WeatherLoggerApplication
import com.example.currentweather.module.WeatherLoggerModule
import com.example.currentweather.module.AppModule
import com.example.currentweather.module.NetworkModule
import com.example.currentweather.ui.activity.MainActivity
import com.example.currentweather.ui.fragment.HomeFragment
import com.example.currentweather.ui.fragment.MoreDetailsFragment
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.android.support.DaggerApplication

@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class, NetworkModule::class, WeatherLoggerModule::class])
interface MainAppComponent : AndroidInjector<DaggerApplication> {
    fun inject(app: WeatherLoggerApplication)
    fun inject(mainActivity: MainActivity)
    fun inject(homeFragment: HomeFragment)
    fun inject(moreDetailsFragment: MoreDetailsFragment)
}