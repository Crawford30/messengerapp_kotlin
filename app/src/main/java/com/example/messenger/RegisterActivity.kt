package com.example.messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button_register.setOnClickListener {
           performRegister()

        }


//=============================Shows Login account on already have an account textview tapped===
        already_have_account_text_view.setOnClickListener {
            Log.d("RegisterActivity", "Try to show login activity")

            ///====launch the log in activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        //=====select  photo Button
        selectphoto_register_button.setOnClickListener{
            Log.d("RegisterActivity", "try to show photo selector")
        }

    }

    private  fun performRegister() {

        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()

        Log.d("RegisterActivity", "Email is" +  email)
        Log.d("RegisterActivity", "Password is $password")

        //===check for the fields not to be empty
        //if (email.isEmpty() || password.isEmpty()) return@setOnClickListener using toast
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please Enter Email Address and Password", Toast.LENGTH_LONG).show()
            return

        }


        //========firebase auth to perform create user with email and password====
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                //else if successfull
                Log.d("Main", "successfully created user with the UId: ${it.result?.user?.uid}")

            }
            .addOnFailureListener {
                Log.d("Main", "Failed to create user ${it.message}")
                Toast.makeText(this, "Failed to create user ${it.message}", Toast.LENGTH_LONG).show()
            }


    }
}
