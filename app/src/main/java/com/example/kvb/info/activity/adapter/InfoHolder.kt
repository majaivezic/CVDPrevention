package com.example.kvb.info.activity.adapter

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kvb.R
import com.example.kvb.home.activity.HomeActivity
import com.example.kvb.info.details.InfoDetailsActivity

class InfoHolder(view: View): RecyclerView.ViewHolder(view){

    val text = itemView.findViewById<TextView>(R.id.info_text)
    var content = ""

    init {
        itemView.setOnClickListener {
            val context = itemView.context
            val intent = Intent(context, InfoDetailsActivity::class.java)
            intent.putExtra("content", content)
            context.startActivity(intent)
        }
    }
}