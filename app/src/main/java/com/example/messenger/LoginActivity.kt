package com.example.messenger

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class LoginActivity: AppCompatActivity() {
    //usually overide on create method

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //-======setting view for the activity=====

        setContentView(R.layout.activity_login)
    }
}