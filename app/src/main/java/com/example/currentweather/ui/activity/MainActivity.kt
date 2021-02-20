package com.example.currentweather.ui.activity

import android.os.Bundle
import androidx.databinding.BindingAdapter
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.currentweather.R
import com.example.currentweather.di.component.DaggerMainAppComponent
import com.example.currentweather.di.component.MainAppComponent
import com.example.currentweather.module.AppModule
import com.example.currentweather.viewmodel.NavigationViewModel
import javax.inject.Inject

class MainActivity : BaseComponentProviderActivity<MainAppComponent>() {

    private lateinit var navController: NavController

    @Inject
    lateinit var navigationViewModel: NavigationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDI()
        initNavGraph()
        observeNavigation()
    }

    private fun initDI() {
        getComponent()?.inject(this)
    }

    fun callClick() {
        displayMessage("Save button clicked")
    }

    private fun initNavGraph() {
        navController = Navigation.findNavController(this, R.id.nav_fragment)
        val navInflater = navController.navInflater
        val graph = navInflater.inflate(R.navigation.navigation_graph)
        graph.startDestination = R.id.homepage
    }

    private fun observeNavigation() {

        navigationViewModel.observeNavigationToMoreScreen().observe(this, Observer {
            navController.navigate(R.id.action_HomeFragment_to_MoreDetailsFragment)
        })

        navigationViewModel.observeNavigationToHomeScreen().observe(this, Observer {
            navController.navigate(R.id.action_MoreDetailsFragment_to_onHomeFragment)
        })


    }

    fun showProgressBar() {

    }

    fun hideProgressBar() {

    }

    override fun getComponent(): MainAppComponent? {
        if (mComponent == null) {
                mComponent = DaggerMainAppComponent.builder().appModule(AppModule(application))
                    .build()
        }
        return mComponent
    }

}
