package com.example.kvb.calculator

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kvb.R
import com.example.kvb.calculator.result.ResultActivity
import kotlinx.android.synthetic.main.activity_calculator.*
import com.example.kvb.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class CalculatorActivity: AppCompatActivity(){
    val mFirebaseAuth = FirebaseAuth.getInstance()
    val mFirebaseDatabase = FirebaseDatabase.getInstance().reference
    val channelId = "com.example.kvb"
    val description = "notification"
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder : Notification.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_calculator)

        calculatorButton.setOnClickListener {
            validateInput()
        }


    }

    private fun validateInput() {

        val age = age_input.text
        val waist = waist_input.text
        val systolic_bp = systolic_bp_input.text
        val diastolic_bp = diastolic_bp_input.text
        val cholesterol = cholesterol_input.text
        val hdl = hdl_input.text
        val ldl = ldl_input.text
        val trigli = trigli_input.text
        val blood_glucose = blood_glucose_input.text

        if(age.isEmpty() || waist.isEmpty() || systolic_bp.isEmpty() || diastolic_bp.isEmpty() || cholesterol.isEmpty() || hdl.isEmpty() || ldl.isEmpty() || trigli.isEmpty() || blood_glucose.isEmpty() ){
            Toast.makeText(this, "Please fill out all required fields", Toast.LENGTH_LONG).show()
        } else {
            val age = age_input.text.toString().toInt()
            val absoluteScore = if(age >=40) calculateSCORE() else null
            val relativeScore = calculateRelativeSCORE()
            val metabolicSyndrome = calculateMetabolicSyndrome()
            val highRisk = calculateHighRisk()
            val veryHighRisk = calculateVeryHighRisk()

            val userId = mFirebaseAuth.currentUser?.uid
            if( userId != null){
                val entry = mFirebaseDatabase.child("user").child(userId).child(System.currentTimeMillis().toString())
                entry.child("absoluteRisk").setValue(absoluteScore.toString())
                entry.child("relativeScore").setValue(relativeScore.toString())
            }

            val intent = Intent(this, ResultActivity::class.java)
            intent.putExtra("absoluteScore", absoluteScore)
            intent.putExtra("relativeScore", relativeScore)
            intent.putExtra("metabolicSyndrome", metabolicSyndrome)
            intent.putExtra("highRisk", highRisk)
            intent.putExtra("veryHighRisk", veryHighRisk)
            startActivity(intent)

            val pendingIntent = PendingIntent.getActivity(this, 0 , intent, PendingIntent.FLAG_UPDATE_CURRENT)

            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationChannel = NotificationChannel(channelId,description,NotificationManager.IMPORTANCE_HIGH)
                notificationChannel.enableLights(true)
                notificationChannel.lightColor = Color.GREEN
                notificationChannel.enableVibration(false)
                notificationManager.createNotificationChannel(notificationChannel)

                builder = Notification.Builder(this, channelId)
                    .setStyle(Notification.BigTextStyle()
                        .bigText("You just calculated your risk score! Remember to maintain healthy lifestyle and check risk score regularly"))
                    .setContentTitle("CVD Prevention")
                    .setContentText("You just calculated your risk score! Remember to maintain healthy lifestyle and check risk score regularly")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)

            }else {
                builder = Notification.Builder(this)
                    .setStyle(Notification.BigTextStyle()
                        .bigText("You just calculated your risk score! Remember to maintain healthy lifestyle and check risk score regularly"))
                    .setContentTitle("CVD Prevention")
                    .setContentText("You just calculated your risk score! Remember to maintain healthy lifestyle and check risk score regularly")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
            }
            notificationManager.notify(1234, builder.build())
        }
    }

    private fun calculateSCORE(): Int {

        val gender = if (male_rb.isChecked) "male" else "female"
        val smoking_status = smoking_status_true.isChecked
        val age = age_input.text.toString().toInt()
        val systolic_bp = systolic_bp_input.text.toString().toInt()
        val cholesterol = cholesterol_input.text.toString().toFloat()
        val hdl = hdl_input.text.toString().toFloat()


        val a = getAge(age)
        val b = Smoking(smoking_status)
        val c = SystolicBP(systolic_bp)
        val d = TC(cholesterol)
        val hcol = HDL(hdl)

        return if (gender === "female") {
             when (hcol) {
                1 ->  W1[a][b][c][d]
                2 ->  W2[a][b][c][d]
                3 ->  W3[a][b][c][d]
                else ->  W4[a][b][c][d]
            }
        } else {
             when (hcol) {
                1 ->  M1[a][b][c][d]
                2 ->  M2[a][b][c][d]
                3 ->  M3[a][b][c][d]
                else ->  M4[a][b][c][d]
            }
        }
    }
    private fun calculateMetabolicSyndrome():Boolean {
        val gender = if (male_rb.isChecked) "male" else "female"
        val trigli = trigli_input.text.toString().toFloat()
        val blood_glucose = blood_glucose_input.text.toString().toFloat()
        val systolic_bp = systolic_bp_input.text.toString().toInt()
        val diastolic_bp = diastolic_bp_input.text.toString().toInt()
        val waist = waist_input.text.toString().toInt()
        val hdl = hdl_input.text.toString().toFloat()

        return (mbRate_bg(blood_glucose) + mbRate_bp(systolic_bp, diastolic_bp) + mbRate_hdl(gender, hdl) + mbRate_trigli(trigli) + mbRate_waist(gender, waist)) >= 3
    }
    private fun calculateRelativeSCORE():Int {
        val smoking_status = smoking_status_true.isChecked
        val systolic_bp = systolic_bp_input.text.toString().toInt()
        val cholesterol = cholesterol_input.text.toString().toFloat()

        val e = Smoking(smoking_status)
        val f = SystolicBP(systolic_bp)
        val g = TC(cholesterol)

        return relative[e][f][g]
    }
    private fun calculateHighRisk():Boolean {

        val ldl = ldl_input.text.toString().toFloat()
        val diastolic_bp = diastolic_bp_input.text.toString().toInt()
        val systolic_bp = systolic_bp_input.text.toString().toInt()
        val cholesterol = cholesterol_input.text.toString().toFloat()

        return diabetes.isChecked || chronic_kidney_disease.isChecked || cholesterol > 8 || systolic_bp >= 180 && diastolic_bp >= 110 || ldl > 6

    }
    private fun calculateVeryHighRisk(): Boolean {
        return cvd.isChecked || serious_chronic_kidney_disease.isChecked || diabetesTOD.isChecked
    }

    private fun getAge(age: Int):Int {
        return when (age) {
            in 40..49 -> 0
            in 50..54 -> 1
            in 55..59 -> 2
            in 60..64 -> 3
            else -> 4
        }
    }
    private fun HDL(hdl: Float):Int {
        return if (hdl <= 0.8f) {
            1
        } else if (hdl > 0.8f && hdl <= 1.0f) {
            2
        } else if (hdl > 1.0f && hdl <= 1.4f) {
            3
        } else {
            4
        }

    }
    private fun Smoking(smoking_status: Boolean):Int {
        return if (smoking_status) {
            1
        } else {
             0
        }
    }
    private fun SystolicBP(systolic_bp: Int):Int {
        return when (systolic_bp) {
            in 161..180 -> 0
            in 141..160 -> 1
            in 121..140 -> 2
            else -> 3
        }


    }
    private fun TC(cholesterol: Float):Int {
        return if (cholesterol >= 4 && cholesterol < 5) {
            0
        } else if (cholesterol >= 5 && cholesterol < 6) {
            1
        } else if (cholesterol >= 6 && cholesterol < 7) {
            2
        } else if (cholesterol >= 7 && cholesterol < 8) {
            3
        } else {
            4
        }
    }

    private fun mbRate_waist(gender: String, waist:Int):Int {
        if(gender === "male" && waist >=102 || gender === "female"&& waist >= 88){
            return 1
        }
        return 0
    }
    private fun mbRate_trigli(trigli:Float):Int {
        if(trigli <= 1.7f){
            return 1
        }
        return 0
    }
    private fun mbRate_hdl(gender:String, hdl:Float):Int{
        if(gender === "male" && hdl <=1f || gender === "female" && hdl<=1.3f){
            return 1
        }
        return 0
    }
    private fun mbRate_bp(systolic_bp:Int, diastolic_bp:Int):Int{
        if(systolic_bp >= 130 && diastolic_bp >= 85){
            return 1
        }
        return 0
    }
    private fun mbRate_bg(blood_glucose:Float):Int {
        if(blood_glucose >= 6.1f){
            return 1
        }
        return 0
    }





}