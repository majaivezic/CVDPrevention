package com.example.kvb.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kvb.R
import com.example.kvb.home.activity.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity: AppCompatActivity(){
    val mFirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        registerButton.setOnClickListener {
            handleRegister()
        }
    }

    private fun handleRegister(){

        val email = email_input.text.toString()
        val password = pass_input.text.toString()

        if(email.isEmpty()){
            email_input.error = "Please enter email!"
            email_input.requestFocus()
        }else if(password.isEmpty()){
            pass_input.error = "Please enter password!"
            pass_input.requestFocus()
        }else if(!(email.isEmpty() && password.isEmpty())){
            mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if(task.isSuccessful){
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}