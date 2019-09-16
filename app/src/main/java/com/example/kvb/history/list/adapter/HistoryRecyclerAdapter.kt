package com.example.kvb.history.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kvb.R
import com.example.kvb.history.list.data.HistoryListItem
import com.example.kvb.info.activity.adapter.InfoHolder


class HistoryRecyclerAdapter: RecyclerView.Adapter<HistoryHolder>(){

    val data = mutableListOf<HistoryListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return HistoryHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        holder.date.text = data[position].date
        holder.absScore.text = data[position].absoluteScore.toString()
        holder.relScore.text = data[position].relativeScore.toString()
    }

    fun setData(data: List<HistoryListItem>){
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
}
