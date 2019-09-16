package com.example.kvb.history.list.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kvb.R

class HistoryHolder(view: View): RecyclerView.ViewHolder(view){

    val date = itemView.findViewById<TextView>(R.id.history_date)
    val absScore = itemView.findViewById<TextView>(R.id.history_absScore)
    val relScore = itemView.findViewById<TextView>(R.id.history_relScore)

}