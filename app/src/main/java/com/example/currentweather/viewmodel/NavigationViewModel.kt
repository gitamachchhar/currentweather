package com.example.currentweather.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData


class NavigationViewModel(application: Application) : BaseCoroutinesViewModel(application) {

    private var navigateToMoreScreenFromHomeScreen = SingleLiveEvent<Boolean>()
    private var navigateToHomeScreenFromMoreScreen = SingleLiveEvent<Boolean>()


    fun observeNavigationToMoreScreen(): LiveData<Boolean> {
        return navigateToMoreScreenFromHomeScreen
    }

    fun observeNavigationToHomeScreen(): LiveData<Boolean> {
        return navigateToHomeScreenFromMoreScreen
    }

    fun goToMoreScreenFromHomePage() {
        navigateToMoreScreenFromHomeScreen.hasActiveObservers()
        navigateToMoreScreenFromHomeScreen.postValue(true)
    }

    fun goToHomeScreenFromMorePage() {
        navigateToHomeScreenFromMoreScreen.postValue(true)
    }

}