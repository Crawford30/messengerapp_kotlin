package com.example.messenger

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity: AppCompatActivity() {
    //usually overide on create method

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //======setting view for the activity=====
        setContentView(R.layout.activity_login)

        login_text_view.setOnClickListener {
            ///====launch the login activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}