package com.example.currentweather.ui.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import androidx.lifecycle.Observer
import com.example.currentweather.BuildConfig
import com.example.currentweather.R
import com.example.currentweather.databinding.FragmentHomeBinding
import com.example.currentweather.helper.CurrentWeatherUtils
import com.example.currentweather.viewmodel.CurrentWeatherViewModel
import com.example.currentweather.viewmodel.LocationViewModel
import com.nabinbhandari.android.permissions.PermissionHandler
import com.nabinbhandari.android.permissions.Permissions
import javax.inject.Inject


class HomeFragment : BaseViewBindingFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var weatherViewModel: CurrentWeatherViewModel

    @Inject
    lateinit var locationViewModel: LocationViewModel

    override fun getLayout(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        requestLocation()
        observeLocationEnabled()
        observeLocationData()
        onErrorObserver()
        observeCurrentWeatherData()
    }

    private fun observeLocationData() {
        locationViewModel.getUserLocation().observe(viewLifecycleOwner, Observer {
            if (it!= null) {
                getCurrentWeatherData(it.latitude, it.longitude)
            }

        })
    }

    private fun getCurrentWeatherData(lat: Double, long: Double) {
        context?.let { weatherViewModel.fetchCurrentWeatherDataByLocation(it, lat.toString(), long.toString(), BuildConfig.WEATHER_KEY) }
    }

    private fun observeCurrentWeatherData() {
        weatherViewModel.getCurrentWeatherLiveData().observe(viewLifecycleOwner, Observer {
            binding?.dateValue = CurrentWeatherUtils.getCurrentFormattedDate()
            binding?.feelValue = CurrentWeatherUtils.kelvinToCelsius(it.main?.feelsLike?:0.0).toString()
            binding?.tempValue = CurrentWeatherUtils.kelvinToCelsius(it.main?.temp?:0.0).toString()
        })
    }

    private fun onErrorObserver() {
        weatherViewModel.getErrorMessage().observe(viewLifecycleOwner, Observer {
            displayMessage(it)
        })
    }

    private fun observeLocationEnabled() {
        locationViewModel.getLocationEnabled().observe(viewLifecycleOwner, Observer {
            if (it) {
                locationViewModel.openEnableLocation(this, 102)
            }
        })
    }

    private fun requestLocation() {
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        val rationale = "Please provide location permission to continue"
        val options = Permissions.Options()
            .setRationaleDialogTitle("Info")
            .setSettingsDialogTitle("Warning")

        Permissions.check(context, permissions, rationale, options, object : PermissionHandler() {
            override fun onGranted() {
                locationViewModel.getLatLong()
            }

            override fun onDenied(context: Context, deniedPermissions: ArrayList<String>) {
                openSettings()
            }
        })
    }

    override fun initDagger() {
        getComponent()?.inject(this)
    }

    fun openSettings() {
        locationViewModel.gotToSettingsToGrantPermission(this, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            requestLocation()
        } else if (requestCode == 102) {
            locationViewModel.getLatLong()
        }
    }
}