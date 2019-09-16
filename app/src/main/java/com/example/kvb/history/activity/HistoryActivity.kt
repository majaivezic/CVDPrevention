package com.example.kvb.history.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kvb.R
import com.example.kvb.history.activity.adapter.HistoryPagerAdapter
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_history)
        history_viewpager.adapter = HistoryPagerAdapter(supportFragmentManager )
    }
}