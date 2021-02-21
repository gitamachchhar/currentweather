package com.example.currentweather.di.qualifiers

import javax.inject.Qualifier


/**
 * qualifier to hit http://api.openweathermap.org/
 */

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseOkHttpBuilder