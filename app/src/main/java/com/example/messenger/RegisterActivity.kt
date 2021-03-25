package com.example.messenger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

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

            //======start photo selector

            //intent for photo selector
            val intent = Intent(Intent.ACTION_PICK)

            //specifying the type
            intent.type = ("image/*")


            //start activity for result, for the result that comes and request code, we gonna use 0
            startActivityForResult(intent, 0)



        }

    }

var selectedPhotoUri: Uri? = null  //global variable, because we need to access it

//we capture the result from photo selector
//the method is called when the intent of photo selector is finished
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    //===Do a couple of checks

    if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
        //1. we proceed and check what selected image was ---
        Log.d("RegisterActivity", "photo was selected")

            //2. we have to figure which photo it is inside out app, the pass data has data, uri will represent the location of where the imahe is stored in the device

         selectedPhotoUri = data.data
        //3. we can use the uri to get access to the image as a bitmap

        val bitmap =  MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

        //4. pace it inside the button
//          val bitmapDrawable = BitmapDrawable(bitmap)
//         selectphoto_register_button.setBackgroundDrawable(bitmapDrawable)


        select_photo_imageview_register.setImageBitmap(bitmap)

        //set phot nutton alpho to 0 to prevent hiding
        selectphoto_register_button.alpha = 0f



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
                Log.d("RegisterActivity", "successfully created user with the UId: ${it.result?.user?.uid}")


                //Then we store image to firebase
                uploadImageToFirebaseStorage()

            }
            .addOnFailureListener {
                Log.d("RegisterActivity", "Failed to create user ${it.message}")
                Toast.makeText(this, "Failed to create user ${it.message}", Toast.LENGTH_LONG).show()
            }


    }


    //fun to upload image to firebase
    private fun uploadImageToFirebaseStorage() {

        //==check selected photo uri
        if(selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString() //random string
        val ref =  FirebaseStorage.getInstance().getReference("/images/$filename") //save inside images folder


        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Successfully uploaded image: ${it.metadata?.path}")

                //=====After finishing loading the image, we need to have access to file location(download loaction)

                ref.downloadUrl.addOnSuccessListener {
                    //===file location===
                    Log.d("RegisterActivity", "File location: $it")


                    saveUserToFirebaseDataBase(it.toString())

                }

            }

            .addOnFailureListener{
                //

                Log.d("RegisterActivity", "Failed to save")

            }








    }


    private  fun saveUserToFirebaseDataBase(profileImageUrl: String) {

       val uid = FirebaseAuth.getInstance().uid?:""

      val ref =   FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, user_edittext_register.text.toString(), profileImageUrl)


        ref.setValue(user).
                addOnSuccessListener {

                    Log.d("RegisterActivity", "We saved the user to firebase database")


                }

    }



    ///===Class user ===
    class User(val uid: String, val username:String, val profileImageUrl:String)


}
