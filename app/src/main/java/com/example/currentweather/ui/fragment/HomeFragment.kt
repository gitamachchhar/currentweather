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
import com.example.currentweather.helper.CurrentWeatherUtils.Companion.clickWithDebounce
import com.example.currentweather.helper.CurrentWeatherUtils.Companion.performHapticFeedback
import com.example.currentweather.viewmodel.NavigationViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject


class HomeFragment : BaseViewBindingFragment<FragmentHomeBinding>() {

    @Inject
    lateinit var weatherViewModel: CurrentWeatherViewModel

    @Inject
    lateinit var locationViewModel: LocationViewModel

    @Inject
    lateinit var navigationViewModel: NavigationViewModel

    override fun getLayout(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        setupCardBackground()
        weatherViewModel.getResultFromDB()
        requestLocation()
        observeLocationEnabled()
        observeLocationData()
        onErrorObserver()
        observeCurrentWeatherData()
        handleClick()
    }

    private fun getCurrentWeatherData(lat: Double, long: Double) {
        context?.let { weatherViewModel.fetchCurrentWeatherDataByLocation(it, lat.toString(), long.toString(), BuildConfig.WEATHER_KEY) }
    }

    private fun observeCurrentWeatherData() {
        weatherViewModel.getCurrentWeatherLiveData().observe(viewLifecycleOwner, Observer {
            hideProgressBar()
            binding?.dateValue = CurrentWeatherUtils.getCurrentFormattedDate(it?.date?:0)
            binding?.feelValue = CurrentWeatherUtils.kelvinToCelsius(it?.main?.feelsLike?:0.0)
            binding?.tempValue = CurrentWeatherUtils.kelvinToCelsius(it?.main?.temp?:0.0)
        })
    }

    private fun onErrorObserver() {
        weatherViewModel.getErrorMessage().observe(viewLifecycleOwner, Observer {
            hideProgressBar()
            displayMessage(it)
        })
    }

    private fun observeLocationEnabled() {
        locationViewModel.getLocationEnabled().observe(viewLifecycleOwner, Observer {
            if (!it) {
                locationViewModel.openEnableLocation(this, 102)
            }
        })
    }

    private fun observeLocationData() {
        locationViewModel.getUserLocation().observe(viewLifecycleOwner, Observer {
            hideProgressBar()
            if (it!= null) {
                showProgressBar()
                getCurrentWeatherData(it.latitude, it.longitude)
            } else {
                displayMessage("Error in fetching current location")
            }

        })
    }

    fun requestLocation() {
        val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        val rationale = "Please provide location permission to continue"
        val options = Permissions.Options()
            .setRationaleDialogTitle("Info")
            .setSettingsDialogTitle("Warning")

        Permissions.check(context, permissions, rationale, options, object : PermissionHandler() {
            override fun onGranted() {
                showProgressBar()
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

    private fun openSettings() {
        locationViewModel.gotToSettingsToGrantPermission(this, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            requestLocation()
        } else if (requestCode == 102) {
            showProgressBar()
            locationViewModel.getLatLong()
        }
    }

    private fun handleClick() {
        binding?.ivReadMore?.clickWithDebounce {
            context?.let { performHapticFeedback(it) }
            navigationViewModel.goToMoreScreenFromHomePage()
        }
    }

    private fun setupCardBackground() {
        if (CurrentWeatherUtils.isDayTime())
            cl_main.background = resources.getDrawable(R.drawable.bg_gradient_day)
        else
            cl_main.background = resources.getDrawable(R.drawable.bg_gradient_night)
    }
}