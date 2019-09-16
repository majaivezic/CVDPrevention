package com.example.kvb.home.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kvb.R
import com.example.kvb.home.activity.adapter.HomePagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        activity_main_viewpager.adapter = HomePagerAdapter(supportFragmentManager)
    }
}