package com.example.currentweather.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import com.example.currentweather.helper.CurrentWeatherUtils
import com.example.currentweather.model.CurrentWeatherModel
import com.example.currentweather.model.Resource
import com.example.currentweather.model.Status
import com.example.currentweather.remote.CurrentWeatherService

class CurrentWeatherViewModel(application: Application, private val service: CurrentWeatherService) : BaseCoroutinesViewModel(application) {

    private var currentWeatherLiveData = SingleLiveEvent<CurrentWeatherModel>()
    private var error = SingleLiveEvent<String>()

    fun getCurrentWeatherLiveData() : LiveData<CurrentWeatherModel> {
        return currentWeatherLiveData
    }

    fun getErrorMessage() : LiveData<String> {
        return error
    }

    fun fetchCurrentWeatherDataByLocation(context: Context, lat: String, long: String, appId: String) {
        if (CurrentWeatherUtils.isNetworkConnected(context)) {
            launchNetworkJob(networkRequest = {service.getCurrentWeatherData(lat, long, appId)},
                onResult = {
                    result ->
                    parseCurrentWeatherResult(result)
                })

        }

    }

    private fun parseCurrentWeatherResult(result: Resource<CurrentWeatherModel>) {
        when (result.status) {
            Status.SUCCESS -> {
                currentWeatherLiveData.postValue(result.data)
            }
            Status.ERROR -> {
                error.postValue(result.message)
            }
            Status.LOADING -> {

            }
        }
    }
}