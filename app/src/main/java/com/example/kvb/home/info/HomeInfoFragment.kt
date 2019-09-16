package com.example.kvb.home.info

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kvb.R
import com.example.kvb.info.activity.InfoActivity
import kotlinx.android.synthetic.main.fragment_home_item.view.*

class HomeInfoFragment: Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.item_name.text = "INFO"

        view.setOnClickListener {

            val intent = Intent(context, InfoActivity::class.java)
            startActivity(intent)
        }
    }
}