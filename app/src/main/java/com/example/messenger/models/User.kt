package com.example.messenger.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

///===Class user ===

@Parcelize
class User(val uid: String, val username:String, val profileImageUrl:String):Parcelable {
    constructor():this("","","") //solves no argument constructor error
}