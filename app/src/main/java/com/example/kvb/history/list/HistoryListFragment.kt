package com.example.kvb.history.list

import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kvb.R
import com.example.kvb.history.list.adapter.HistoryRecyclerAdapter
import com.example.kvb.history.list.data.HistoryListItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_history_list.*


class HistoryListFragment: Fragment(){

    private val adapter = HistoryRecyclerAdapter()
    val mFirebaseDatabase = FirebaseDatabase.getInstance().reference
    val mFirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_history_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        history_list_recycler.layoutManager = LinearLayoutManager(context)
        history_list_recycler.adapter = adapter
        getData()

    }

    private fun getData(){
        val userId = mFirebaseAuth.currentUser?.uid

        if(userId != null){
            mFirebaseDatabase.child("user").child(userId).addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    Toast.makeText(this@HistoryListFragment.context, p0.message, Toast.LENGTH_LONG).show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.value != null) {
                        val data = p0.value as Map<String, Map<String, String>>

                        val adapterData = data.map {
                            val date = getDate(it.key)
                            val dateMilis = it.key.toLong()
                            val abs = "Absolute score: " + it.value["absoluteRisk"]
                            val rel = "Relative score: " + it.value["relativeScore"]!!
                            HistoryListItem(dateMilis, date, abs, rel)
                        }.sortedByDescending { it.dateMilis }
                        adapter.setData(adapterData)
                    }


                }



            })
        }

    }

    private fun getDate(date: String): String {
        return DateFormat.format("dd-MM-yyyy", date.toLong()).toString()
    }
}