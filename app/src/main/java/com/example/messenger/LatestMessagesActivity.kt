package com.example.messenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class LatestMessagesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

       verifyUserIsLoggedIn()



    }





//=========CHECKED IF USER IS LOGGED IN ========
  private fun verifyUserIsLoggedIn() {
        //===check if user is logged in ====

        val uid = FirebaseAuth.getInstance().uid


        if(uid == null) {
            //====user not logged in, redirect to register ====
            val intent = Intent(this,RegisterActivity::class.java)
            //clear all activities on the stack
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)

            startActivity(intent)
        }

    }

//=======Option item selected====
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId){
        R.id.menu_new_message -> {

            val intent = Intent(this,NewMessageActivity::class.java)
            startActivity(intent)


        }


        R.id.menu_sign_out -> {

            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this,RegisterActivity::class.java)
            //clear all activities on the stack
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)


        }
    }
        return super.onOptionsItemSelected(item)
    }



    //====option menu=====
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        //inflate the menu resoure
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}

