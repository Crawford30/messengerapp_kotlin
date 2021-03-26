package com.example.messenger.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.messenger.NewMessageActivity
import com.example.messenger.R
import com.example.messenger.UserItem
import com.example.messenger.models.ChatMessage
import com.example.messenger.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatLogActivity : AppCompatActivity() {


    //===static constant ===
    companion object{
        val TAG = "ChatLog"

    }

    val adapter = GroupAdapter<GroupieViewHolder>()

    var toUser: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        recyclerview_chat_log.adapter = adapter

        //supportActionBar?.title = "Chat Log"
        //get the name

        // val username =   intent.getStringExtra(NewMessageActivity.USER_KEY)

        toUser = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)

        supportActionBar?.title = toUser?.username

        listensForMessages()

        //setUpDummyData()


        send_button_chat_log.setOnClickListener {
            Log.d(TAG, "Attempt to Send message")
            performSendMessage()
        }


    }



    private fun listensForMessages() {
       // val ref = FirebaseDatabase.getInstance().getReference("/messages")

        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")



        //Its more real time when we use  addChildEventListener
        ref.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

               val chatMessage =  snapshot.getValue(ChatMessage::class.java)

                if(chatMessage != null) {

                    Log.d(TAG, chatMessage?.text)

//                    //====add objects inside adapter
//                    adapter.add(ChatFromItem(chatMessage.text))

                    //====check for fromid and toId to know which layout to present

                    if(chatMessage.fromId == FirebaseAuth.getInstance().uid) {

                        val currentUser = LatestMessagesActivity.currentUser ?: return //elvis operator

                        //====add objects inside adapter
                        //we use toitem, placed on the left
                        adapter.add(ChatFromItem(chatMessage.text, currentUser))

                    } else {

                        //val toUser  = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)

                        //we use toitem, placed on the right
                        adapter.add(ChatToItem(chatMessage.text,toUser!!))

                    }

                }


            }


            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
        })


    }

//    class ChatMessage(val id:String, val text: String, val fromId:String,val toId:String, val timeStamp:Long ){
//        constructor(): this("","","","",-1)
//
//    }

    private fun performSendMessage() {

        val textMessage = edit_text_chat_log.text.toString()

        val fromId  = FirebaseAuth.getInstance().uid
        if (fromId == null) return


        //elvis operator
       // val fromId  = FirebaseAuth.getInstance().uid ?: return

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = user.uid
        //====How do we send message to firebase

        //1. get firebase reference
      //  val reference = FirebaseDatabase.getInstance().getReference("/messages").push() //to push will generate automatic node for us in rtd


        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push() //to push will generate automatic node for us in rtd


        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push() //to push will generate automatic node for us in rtd


        val chatMessage = ChatMessage(reference.key!!, textMessage, fromId, toId!!, System.currentTimeMillis()/1000)
        //2. Access the reference and set some value
        reference.setValue(chatMessage).
                addOnSuccessListener {
                    Log.d(TAG, "Our Chat Message Saved Successfully: ${reference.key}")

                    //-=====clear the text after send tapped===

                    edit_text_chat_log.text.clear()

                    //========go to the last message after hitting send

                    recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1)

                }


        toReference.setValue(chatMessage)
    }



}


class ChatFromItem(val text:String, val currentUser: User): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        //viewHolder.itemView.text_view_from_row.text = "From message..."

        viewHolder.itemView.text_view_from_row.text = text

        //====load user image ====

        val uri = currentUser.profileImageUrl

        val targetImageView =viewHolder.itemView.image_view_chat_from_row

        Picasso.get().load(uri).into(targetImageView)



    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row

    }
}


class ChatToItem(val text: String, val user:User): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

//        viewHolder.itemView.text_view_to_row.text = "This is to row text message that is longer..."


        viewHolder.itemView.text_view_to_row.text =  text

        //====load user image ====

        val uri = user.profileImageUrl

        val targetImageView =viewHolder.itemView.image_view_chat_to_row

        Picasso.get().load(uri).into(targetImageView)

    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row

    }
}