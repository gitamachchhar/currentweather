package com.example.currentweather.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.example.currentweather.di.component.MainAppComponent
import com.example.currentweather.helper.AutoClearedValue
import com.example.currentweather.ui.activity.BaseComponentProviderActivity
import com.example.currentweather.ui.activity.MainActivity


abstract class BaseViewBindingFragment<B : ViewDataBinding> : Fragment() {

    private var mComponent: MainAppComponent? = null

    var binding: B? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val dataBinding: B = DataBindingUtil.inflate(inflater, getLayout(), container, false)
        binding = AutoClearedValue(this, dataBinding).get()
        mComponent = (activity as BaseComponentProviderActivity<*>).getComponent()
        initDagger()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    abstract fun getLayout(): Int

    abstract fun initView()

    fun showProgressBar() {
        (activity as MainActivity).showProgressBar()
    }

    fun hideProgressBar() {
        (activity as MainActivity).hideProgressBar()
    }

    fun displayMessage(message: String) {
        (activity as MainActivity).displayMessage(message)
    }

    fun getComponent() = mComponent

    abstract fun initDagger()

}