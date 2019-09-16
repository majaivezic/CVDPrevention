package com.example.kvb.info.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kvb.R
import com.example.kvb.info.activity.adapter.InfoRecyclerAdapter
import com.example.kvb.info.activity.data.InfoListItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity: AppCompatActivity(){

    val adapter = InfoRecyclerAdapter()
    val mFirebaseDatabase = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_info)

        info_recycler.layoutManager = LinearLayoutManager(this)
        info_recycler.adapter = adapter
        getData()
    }

    private fun getData(){
        mFirebaseDatabase.child("info").addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@InfoActivity, p0.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(p0: DataSnapshot) {


                val data = p0.value as Map<String, String>
                val adapterData = data.map { InfoListItem(it.key, it.value) }
                adapter.setData(adapterData)
            }

        })
    }


}