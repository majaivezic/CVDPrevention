package com.example.kvb.history.activity.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.kvb.history.chart.HistoryChartFragment
import com.example.kvb.history.list.HistoryListFragment

class HistoryPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm){
    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> HistoryListFragment()
            else -> HistoryChartFragment()

        }
    }

    override fun getCount(): Int {
        return 2
    }


}