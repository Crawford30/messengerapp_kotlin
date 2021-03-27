package com.example.messenger.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.messenger.NewMessageActivity
import com.example.messenger.R
import com.example.messenger.models.ChatMessage
import com.example.messenger.models.User
import com.example.messenger.registerlogin.RegisterActivity
import com.example.messenger.views.LatestMessageRow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_latest_messages.*
import kotlinx.android.synthetic.main.latest_message_row.view.*

class LatestMessagesActivity : AppCompatActivity() {

    //====current user logged in-===
    companion object {
        var currentUser: User? = null
        val TAG = "LatestMessages"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_messages)

        recycler_view_latest_messages.adapter = adapter
        //decoration
        recycler_view_latest_messages.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        //===========OnClick Listener on recycler(Set item click listener), on the group adapter
        adapter.setOnItemClickListener { item, view ->

            //the item is in term of the row

            Log.d(TAG, "1234")

            //====we are missing the chat partner user
            val row = item as LatestMessageRow //safe casting to Latest Message Row



            val intent = Intent(view.context, ChatLogActivity::class.java)
            intent.putExtra(NewMessageActivity.USER_KEY, row.chatPartnerUser)
            startActivity(intent)

        }






        //setUpDummyRows()
        listensForLatestMessages()

        fetchCurrentUser()

       verifyUserIsLoggedIn()



    }



    val latestMessagesMap = HashMap<String, ChatMessage>()


    private fun refreshRecyclerViewMessages() {
        adapter.clear() //all the old messages will be cleared up
        latestMessagesMap.values.forEach{
            adapter.add(LatestMessageRow(it)) //it refers to one of the item inside the loop

        }
    }


    private fun listensForLatestMessages(){
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")

        //===listens for new node under the ref
        ref.addChildEventListener(object: ChildEventListener{

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                ///we will be notified each we see a new child
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return

                //latest map
                latestMessagesMap[snapshot.key!!]  = chatMessage //the key is the recipient user id


                //we refresh recycler view each time we add a chat
                refreshRecyclerViewMessages()

                //add to adapter

                //adapter.add(LatestMessageRow(chatMessage))


            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                //when changed from firebase, should reflect
                //its called every time a node is changed
                //we will be notified each we see a new child
                val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return

                //latest map
                latestMessagesMap[snapshot.key!!]  = chatMessage //the key is the recipient user id


                //we refresh recycler view each time we add a chat
                refreshRecyclerViewMessages()



            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

    }



    val adapter = GroupAdapter<GroupieViewHolder>()

//    private fun setUpDummyRows() {
//
//
//        adapter.add(LatestMessageRow())
//        adapter.add(LatestMessageRow())
//        adapter.add(LatestMessageRow())
//        adapter.add(LatestMessageRow())
//
//
//
//    }


    private fun fetchCurrentUser(){

        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        ref.addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
                Log.d("LatestMessage","Current User: ${currentUser?.username}")

            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }





//=========CHECKED IF USER IS LOGGED IN ========
  private fun verifyUserIsLoggedIn() {
        //===check if user is logged in ====

        val uid = FirebaseAuth.getInstance().uid


        if(uid == null) {
            //====user not logged in, redirect to register ====
            val intent = Intent(this, RegisterActivity::class.java)
            //clear all activities on the stack
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)

            startActivity(intent)
        }

    }

//=======Option item selected====
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when(item.itemId){
        R.id.menu_new_message -> {

            val intent = Intent(this, NewMessageActivity::class.java)
            startActivity(intent)


        }


        R.id.menu_sign_out -> {

            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this, RegisterActivity::class.java)
            //clear all activities on the stack
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)


        }
    }
        return super.onOptionsItemSelected(item)
    }



    //====option menu=====
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        //inflate the menu resource
        menuInflater.inflate(R.menu.nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }
}

