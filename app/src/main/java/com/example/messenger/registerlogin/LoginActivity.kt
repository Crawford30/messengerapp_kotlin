package com.example.messenger.registerlogin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.messenger.R
import com.example.messenger.messages.LatestMessagesActivity
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
            val intent = Intent(this, RegisterActivity::class.java)
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


                    val intent = Intent(this, LatestMessagesActivity::class.java)

                    //clear all activities on the stack
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)

                }
                .addOnFailureListener {
                    Log.d("Login", "Failed to Login ${it.message}")
                }

    }
}