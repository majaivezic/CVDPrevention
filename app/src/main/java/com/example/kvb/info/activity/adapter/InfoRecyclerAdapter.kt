package com.example.kvb.info.activity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import com.example.kvb.R
import com.example.kvb.info.activity.data.InfoListItem


class InfoRecyclerAdapter(): RecyclerView.Adapter<InfoHolder>(){

    val data = mutableListOf<InfoListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_info, parent, false)
        return InfoHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: InfoHolder, position: Int) {
        holder.text.text = data[position].title
        holder.content = data[position].content
    }

    fun setData(list: List<InfoListItem>){
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }
}