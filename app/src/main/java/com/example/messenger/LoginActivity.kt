package com.example.messenger

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {
    //usually overide on create method

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //======setting view for the activity=====
        setContentView(R.layout.activity_login)

        //calling fun to perform login
        button_login.setOnClickListener {
            performLogin()
        }

        //=========on Back to login text view clicked

        back_to_register_text_view.setOnClickListener {

            ///====launch the login activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }




    private  fun performLogin() {

            val email = email_login.text.toString()
            val password = password_login.text.toString()

        if (email.isEmpty() || password.isEmpty()) {

             // checks for email and password text field to make sure they are not empty
            Toast.makeText(this, "Please Enter Email Address and Password", Toast.LENGTH_LONG).show()
            return

        }

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

    }
}