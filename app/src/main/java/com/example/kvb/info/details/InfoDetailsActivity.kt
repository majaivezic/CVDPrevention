package com.example.kvb.info.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kvb.R
import kotlinx.android.synthetic.main.activity_info_details.*

class InfoDetailsActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_info_details)
        content.text = intent.getStringExtra("content")
    }
}