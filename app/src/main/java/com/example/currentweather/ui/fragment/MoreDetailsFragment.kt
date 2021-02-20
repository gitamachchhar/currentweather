package com.example.currentweather.ui.fragment

import com.example.currentweather.R
import com.example.currentweather.databinding.FragmentMoredetailsBinding

class MoreDetailsFragment : BaseViewBindingFragment<FragmentMoredetailsBinding>() {
    override fun getLayout(): Int {
        return R.layout.fragment_moredetails
    }

    override fun initView() {

    }

    override fun initDagger() {
        getComponent()?.inject(this)
    }

}