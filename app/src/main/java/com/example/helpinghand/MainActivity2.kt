package com.example.helpinghand

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.example.helpinghand.adapters.NetworkUtils

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, AllPostsFragmant())
                .commit()
        }


        if (NetworkUtils.isNetworkAvailable(this)) {
            // Network is available, continue with app startup
        } else {
            // Network is not available, show an error message or take appropriate action
            Toast.makeText(this@MainActivity2, "Please check your network connection", Toast.LENGTH_LONG).show()
        }

        val fragmentName = intent.getStringExtra("fragment_to_load")

//        if (fragmentName == "AllPostsFragment") {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragmentContainerView, AllPostsFragmant())
//                .commit()
//        }

        val HomeBtn:ImageView = findViewById(R.id.imgBtnHome)
        val ChatBtn :ImageView = findViewById(R.id.imgBtnChat)
        val PostsBtn:ImageView = findViewById(R.id.imgBtnNewPost)
        val MyPostsBtn:ImageView = findViewById(R.id.imgBtnMyPosts)
        val ProfileBtn:ImageView = findViewById(R.id.imgBtnProfile)
        HomeBtn.setImageResource(R.drawable.home_selected)
        ChatBtn.setImageResource(R.drawable.chat_notselected)
        PostsBtn.setImageResource(R.drawable.newpost_notselected)
        MyPostsBtn.setImageResource(R.drawable.posts_notselected)
        ProfileBtn.setImageResource(R.drawable.profile_noselected)


        HomeBtn.setOnClickListener {

            HomeBtn.setImageResource(R.drawable.home_selected)
            ChatBtn.setImageResource(R.drawable.chat_notselected)
            PostsBtn.setImageResource(R.drawable.newpost_notselected)
            MyPostsBtn.setImageResource(R.drawable.posts_notselected)
            ProfileBtn.setImageResource(R.drawable.profile_noselected)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView,AllPostsFragmant())
                commit()
            }
        }

        ProfileBtn.setOnClickListener {

            HomeBtn.setImageResource(R.drawable.home_notselected)
            ChatBtn.setImageResource(R.drawable.chat_notselected)
            PostsBtn.setImageResource(R.drawable.newpost_notselected)
            MyPostsBtn.setImageResource(R.drawable.posts_notselected)
            ProfileBtn.setImageResource(R.drawable.profile_selected)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView,ProfileFragment())
                commit()
            }
        }

        PostsBtn.setOnClickListener {

            HomeBtn.setImageResource(R.drawable.home_notselected)
            ChatBtn.setImageResource(R.drawable.chat_notselected)
            PostsBtn.setImageResource(R.drawable.newpost_selected)
            MyPostsBtn.setImageResource(R.drawable.posts_notselected)
            ProfileBtn.setImageResource(R.drawable.profile_noselected)


            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView,PostFragment())
                commit()
            }
        }

        MyPostsBtn.setOnClickListener {

            HomeBtn.setImageResource(R.drawable.home_notselected)
            ChatBtn.setImageResource(R.drawable.chat_notselected)
            PostsBtn.setImageResource(R.drawable.newpost_notselected)
            MyPostsBtn.setImageResource(R.drawable.posts_selected)
            ProfileBtn.setImageResource(R.drawable.profile_noselected)


            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView,myPostsFragment())
                commit()
            }
        }

        ChatBtn.setOnClickListener {

            HomeBtn.setImageResource(R.drawable.home_notselected)
            ChatBtn.setImageResource(R.drawable.chat_selected)
            PostsBtn.setImageResource(R.drawable.newpost_notselected)
            MyPostsBtn.setImageResource(R.drawable.posts_notselected)
            ProfileBtn.setImageResource(R.drawable.profile_noselected)

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView,ChatFragment())
                commit()
            }
        }

    }
}