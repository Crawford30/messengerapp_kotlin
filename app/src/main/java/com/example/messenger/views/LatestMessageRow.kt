package com.example.messenger.views

import com.example.messenger.R
import com.example.messenger.models.ChatMessage
import com.example.messenger.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.latest_message_row.view.*


class LatestMessageRow(val chatMessage: ChatMessage): Item<GroupieViewHolder>(){

    var chatPartnerUser:User? = null

    override fun getLayout(): Int {

        return R.layout.latest_message_row

    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {



        viewHolder.itemView.message_text_view_latest_message.text = chatMessage.text

//https://youtu.be/gaeTFNqKA2M?list=PL0dzCUj1L5JE-jiBHjxlmXEkQkum_M3R-&t=678
        val chatPartnerId:String

        if(chatMessage.fromId == FirebaseAuth.getInstance().uid) {
            //=====currently logged in user====

            chatPartnerId = chatMessage.toId

        }
        else {
            chatPartnerId = chatMessage.fromId
        }

        //===fetch the user
        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")


        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                chatPartnerUser = snapshot.getValue(User::class.java)

                viewHolder.itemView.message_text_view_latest_message.text =  chatPartnerUser?.username

                val targetImageView = viewHolder.itemView.message_image_view_latest_image_view

                //https://stackoverflow.com/questions/42714132/how-to-make-picasso-display-default-image-when-there-is-an-invalid-path
                //https://stackoverflow.com/questions/55441242/picasso-showing-the-error-path-must-not-be-empty


                if(chatPartnerUser?.profileImageUrl?.trim()?.isEmpty() == true) {
                    targetImageView.setImageResource(R.drawable.commanderlogo)

                } else {

                    Picasso.get().load(chatPartnerUser?.profileImageUrl).into(targetImageView)

                }

//                    Picasso.get()
//                        .load(user?.profileImageUrl)
//                        .placeholder(R.drawable.ic_launcher_background)
//                        .into(targetImageView)
                //load(user?.profileImageUrl).into(targetImageView)
                // .error(R.drawable.commanderlogo)



            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        //viewHolder.itemView.message_text_view_latest_message.text = chatMessage.text

//            viewHolder.itemView.message_text_view_username.text = "User"

    }
}
