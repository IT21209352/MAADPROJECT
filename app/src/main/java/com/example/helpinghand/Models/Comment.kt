package com.example.helpinghand.Models

data class Comment(
    var comments_comment : String ?=null ,
    var comments_owner : String ?=null,
    var comment_id : String ?= null,
    val postID : String ?= null,
    val postOwner : String ?= null,
    val postTitle : String ?= null,
    val postPosi: String ?= null,
    val likes : Int?=null,
    val postOwnerID : String ?= null,
    val cmmntOwnerID : String ?= null,

    )