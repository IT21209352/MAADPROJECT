package com.example.helpinghand

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val HomeBtn:ImageView = findViewById(R.id.imgBtn1)
        val ProfileBtn:ImageView = findViewById(R.id.imgBtn2)
        val PostsBtn:ImageView = findViewById(R.id.imgBtn3)
        val MyPostsBtn:ImageView = findViewById(R.id.imgBtn4)
        val ChatBtn:ImageView = findViewById(R.id.imgBtn5)

        HomeBtn.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView,HomeFragment())
                commit()
            }
        }

        ProfileBtn.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView,ProfileFragment())
                commit()
            }
        }

        PostsBtn.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView,PostFragment())
                commit()
            }
        }

        MyPostsBtn.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView,myPostsFragment())
                commit()
            }
        }

        ChatBtn.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainerView,ChatFragment())
                commit()
            }
        }


    }
}