package com.example.messenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        //=====set the title====
        supportActionBar?.title = "Select user"

        //====present a list of user inside recycler view===

        //adapter, we use groupie adapter
        //recyclerview_newmessage.adapter =

        //val adapter = GroupAdapter<RecyclerView.ViewHolder>() //empty constructor

        //val adapter =  GroupAdapter<GroupieViewHolder>()


//        adapter.add(UserItem())
//        adapter.add(UserItem())
//        adapter.add(UserItem())

        //recyclerview_newmessage.adapter = adapter

        fetchUser()



        //====layout manager


        //set from xml
       // recyclerview_newmessage.layoutManager = LinearLayoutManager(this)
    }


    private fun fetchUser() {

      val ref =   FirebaseDatabase.getInstance().getReference("/users")

        ref.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                val adapter =  GroupAdapter<GroupieViewHolder>()

                //====will get called every time we call a user
                snapshot.children.forEach {

                    Log.d("NewMessage", it.toString() )

                    //=convert the value to user object

                    val user = it.getValue(RegisterActivity.User::class.java)

                    if(user != null) {

                        adapter.add(UserItem(user))

                    }




                }

                recyclerview_newmessage.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
}


class  UserItem(val user:RegisterActivity.User):Item<GroupieViewHolder>(){

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.user_name_textview_new_message.text = user.username

        //will be called in our list for user object

        //====load image

        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageview_new_message)

    }

    override fun getLayout(): Int {

        return R.layout.user_row_new_message

    }



}



//=====super tideuos, we user third party library===
//class CustomAdapter:RecyclerView.Adapter<RecyclerView.ViewHolder>{
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        TODO("Not yet implemented")
//    }
//}