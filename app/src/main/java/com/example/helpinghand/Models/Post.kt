package com.example.helpinghand.Models

data class Post(val imageUrl: String? = null,
                val medName : String? = null,
                val medPrice : Float? = null,
                var postDetail: String ?= null,
                val post_owner: String ?= null,
                val post_key: String ?= null,
                val post_ownerID: String ?= null

)