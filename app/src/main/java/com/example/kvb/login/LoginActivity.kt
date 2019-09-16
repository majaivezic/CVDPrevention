package com.example.kvb.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kvb.R
import com.example.kvb.home.activity.HomeActivity
import com.example.kvb.register.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity(){
    var mFirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        loginButton.setOnClickListener {
            handleLogin()
         }

        registerLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleLogin(){
        val email = email_login_input.text.toString()
        val password = pass_login_input.text.toString()

        if(email.isEmpty()){
            email_login_input.error = "Please enter email!"
            email_login_input.requestFocus()
        }else if(password.isEmpty()){
            pass_login_input.error = "Please enter password!"
            pass_login_input.requestFocus()
        }else if(!(email.isEmpty() && password.isEmpty())){
            mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
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
