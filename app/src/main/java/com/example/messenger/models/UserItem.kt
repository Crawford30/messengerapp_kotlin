package com.example.messenger.models

import com.example.messenger.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class  UserItem(val user:User): Item<GroupieViewHolder>(){

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