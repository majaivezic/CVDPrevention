package com.example.kvb.calculator.result

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kvb.R

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_result.*




class ResultActivity: AppCompatActivity(){

    private val mFirebaseDatabase = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_result)
        val absoluteScore = intent.getIntExtra("absoluteScore", -1)
        val relativeScore = intent.getIntExtra("relativeScore", -1)
        val metabolicSyndrome = intent.getBooleanExtra("metabolicSyndrome", false)
        val highRisk = intent.getBooleanExtra("highRisk", false)
        val veryHighRisk = intent.getBooleanExtra("veryHighRisk", false)
        getAdvice()

        if(absoluteScore>=10 || veryHighRisk || metabolicSyndrome){
            risk.text = "Very high risk"
            risk.setBackgroundColor(Color.parseColor("#FBCEB5"))
        }else if(absoluteScore in 5..9 || highRisk){
            risk.text ="High risk"
            risk.setBackgroundColor(Color.parseColor("#FF6A61"))
        }else if(absoluteScore in 2..4){
            risk.text ="Medium risk"
            risk.setBackgroundColor(Color.parseColor("#fdfd96"))
        }else{
            risk.text ="Low risk"
            risk.setBackgroundColor(Color.parseColor("#C9FFBF"))
        }

        if(absoluteScore != -1){
            result.text = "Abosolute score: " + absoluteScore + "." + "Relative score: " + relativeScore
        } else {
            result.text = "Relative score: " + relativeScore
        }
    }
    private fun getAdvice() {
        mFirebaseDatabase.child("advice").addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Toast.makeText(this@ResultActivity, p0.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                advice.text = p0.value.toString()
            }

        })

    }

}