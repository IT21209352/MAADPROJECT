package com.example.helpinghand.Models

object GlobalPostsList {

    private var posts: MutableList<Post> = mutableListOf()

    fun getPosts(): MutableList<Post> {
        return posts
    }

    fun setPosts(postsList: MutableList<Post>) {
        posts = postsList
    }
}
