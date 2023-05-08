package com.example.helpinghand.adapters

import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.helpinghand.HomeFragment
import com.example.helpinghand.Models.Post
import com.example.helpinghand.R
import com.example.helpinghand.myPostsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AllPostsAdaptor(
    private val context: Context,
    private val onChatButtonClickListener: OnChatButtonClickListener,
    private val posts: MutableList<Post>,
    private val fragmentManager: FragmentManager
    ) : RecyclerView.Adapter<AllPostsAdaptor.ViewHolder>() {
    private val colors = arrayOf("#FFCDD2", "#F8BBD0", "#E1BEE7", "#D1C4E9", "#C5CAE9", "#BBDEFB", "#B3E5FC", "#B2EBF2", "#B2DFDB", "#C8E6C9")

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postImage: ImageView = itemView.findViewById(R.id.allPostImageView)
        val postDetail: TextView = itemView.findViewById(R.id.allPostTextView)
        val helpbtn: Button = itemView.findViewById(R.id.allPostBtn)
        val postCardView : CardView = itemView.findViewById(R.id.allPostCardView)
        val cmntBtn: Button = itemView.findViewById(R.id.allPostCmntBtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.a_all_post_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var auth: FirebaseAuth = Firebase.auth
        val post = posts[position]
        val color = colors[position % colors.size]
        val crntUserEmail = auth.currentUser?.email.toString()

        holder.postCardView.setBackgroundColor(Color.parseColor(color))

        // Load the image using Glide or Picasso library
        Glide.with(holder.itemView.context)
            .load(post.imageUrl)
            .into(holder.postImage)

        holder.postDetail.text = post.postDetail

        if (post.post_owner == crntUserEmail){
           holder.cmntBtn.visibility = View.INVISIBLE
        }

        if (post.post_owner == crntUserEmail){
            holder.helpbtn.visibility = View.INVISIBLE
        }

        holder.cmntBtn.setOnClickListener {
            Log.d(ContentValues.TAG, "this is the related comment ${post.post_owner}")

            val postOwner = "Hi, ${post.post_owner} I can help you regarding your post ${post.postDetail}"
            val homeFragment = HomeFragment()
            val bundle = Bundle()
            bundle.putString("postOwner", postOwner)
            homeFragment.arguments = bundle

            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, homeFragment)
            transaction.addToBackStack(null)
            transaction.commit()

        }

        holder.helpbtn.setOnClickListener {
            onChatButtonClickListener.onChatButtonClick(post)
        }
    }

    override fun getItemCount() = posts.size

    // Interface to handle button click
    interface OnChatButtonClickListener {
        fun onChatButtonClick(post: Post)
    }
}