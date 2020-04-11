package com.example.messenger

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {
    //usually overide on create method

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //======setting view for the activity=====
        setContentView(R.layout.activity_login)

        login_text_view.setOnClickListener {

            val email = email_login.text.toString()
            val password = password_login.text.toString()

            //========firebase auth====

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener
                    //======else===
                    Log.d("Login", "successfully created user with the UId: ${it.result?.user?.uid}")
                }
                .addOnFailureListener {
                    Log.d("Login", "Failed to Login ${it.message}")
                }




            ///====launch the login activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}