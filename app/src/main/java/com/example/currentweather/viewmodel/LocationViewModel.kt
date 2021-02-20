package com.example.currentweather.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.example.currentweather.helper.LocationUtil


class LocationViewModel(application: Application) : BaseCoroutinesViewModel(application) {


    private var userLocation = SingleLiveEvent<Location>()

    private var locationEnabled = SingleLiveEvent<Boolean>()

    fun getUserLocation(): LiveData<Location> {
        return userLocation
    }

    fun getLocationEnabled(): LiveData<Boolean> {
        return locationEnabled
    }

    fun getLatLong() {
        if (LocationUtil.isLocationEnabled(getApplication())) {
            LocationUtil.startUserCurrentLocationListener(getApplication()) {
                if (it != null) userLocation.postValue(it) else locationEnabled.postValue(false)
            }

        } else {
            locationEnabled.postValue(false)
        }
    }

    fun openEnableLocation(sourceFragment: Fragment, requestCode: Int) {
        sourceFragment.startActivityForResult(
            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
            requestCode
        )
    }

    fun gotToSettingsToGrantPermission(sourceFragment: Fragment, requestCode: Int) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", sourceFragment.activity?.packageName, null)
        intent.data = uri
        sourceFragment.startActivityForResult(intent, requestCode)
    }
}
