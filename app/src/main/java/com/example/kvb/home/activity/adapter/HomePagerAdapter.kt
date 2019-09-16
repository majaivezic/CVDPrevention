package com.example.kvb.home.activity.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.kvb.home.calculator.HomeCalculatorFragment
import com.example.kvb.home.history.HomeHistoryFragment
import com.example.kvb.home.info.HomeInfoFragment

class HomePagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm){
    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> HomeCalculatorFragment()
            1 -> HomeInfoFragment()
            else -> HomeHistoryFragment()
        }
    }

    override fun getCount(): Int {
        return 3
    }


}