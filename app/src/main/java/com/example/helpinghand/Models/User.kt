package com.example.helpinghand.Models

import android.net.Uri

data class User(
    val email:String? = null,
    val name:String? = null,
    val uid:String? = null,
    val address:String? = null,
    val phone:String? = null,
    val profilePictureResourceId: Int? = null,
    val profilePictureUri: Uri? = null
)
