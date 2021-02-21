package com.example.currentweather.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import com.example.currentweather.helper.CurrentWeatherUtils
import com.example.currentweather.local.WeatherRepository
import com.example.currentweather.model.CurrentWeatherModel
import com.example.currentweather.model.Resource
import com.example.currentweather.model.Status
import com.example.currentweather.remote.CurrentWeatherService
import java.util.*

class CurrentWeatherViewModel(application: Application, private val service: CurrentWeatherService, private val weatherRepository: WeatherRepository) : BaseCoroutinesViewModel(application) {

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

        } else {
            error.postValue("No internet!")
        }

    }

    private fun parseCurrentWeatherResult(result: Resource<CurrentWeatherModel>) {
        when (result.status) {
            Status.SUCCESS -> {
                if (result.data != null) saveDataInLocal(result.data) else error.postValue("No Data Available!")
            }
            Status.ERROR -> {
                error.postValue(result.message)
            }
            Status.LOADING -> {

            }
        }
    }

    private fun saveDataInLocal(currentWeatherModel: CurrentWeatherModel) {
        launchJob {
            currentWeatherModel.date = Calendar.getInstance().timeInMillis
            weatherRepository.insertWeatherData(currentWeatherModel)
            currentWeatherLiveData.postValue(currentWeatherModel)
        }
    }

    fun getResultFromDB() {
        launchDatabaseJob(databaseQuery = {
            weatherRepository.getWeatherData()
        },
            onResult = {
                result -> if (result != null) currentWeatherLiveData.postValue(result)
            })

    }

}