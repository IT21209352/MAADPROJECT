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
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.helpinghand.HomeFragment
import com.example.helpinghand.Models.GlobalPostsList
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
    private val fragmentManager: FragmentManager ) : RecyclerView.Adapter<AllPostsAdaptor.ViewHolder>()
        {private val colors = arrayOf("#E1BEE7","#D1C4E9", "#C5CAE9", "#BBDEFB",
            "#B3E5FC", "#B2EBF2", "#B2DFDB", "#C8E6C9")

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val postImage: ImageView = itemView.findViewById(R.id.allPostImageView)
        val postDetail: TextView = itemView.findViewById(R.id.allPostTextView)
        val helpbtn: Button = itemView.findViewById(R.id.allPostBtn)
        val postCardView : CardView = itemView.findViewById(R.id.allPostCardView)
        val cmntBtn: Button = itemView.findViewById(R.id.allPostCmntBtn)
        val postOwner: TextView = itemView.findViewById(R.id.allPostPostOwner)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.a_all_post_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var auth: FirebaseAuth = Firebase.auth

        GlobalPostsList.setPosts(posts)

        val post = posts[position]

        val color = colors[position % colors.size]

        val oppositeColor = ColorUtils.blendARGB(Color.parseColor(color), Color.WHITE, 0.5f)

        val crntUserEmail = auth.currentUser?.email.toString()

        holder.postCardView.setBackgroundColor(oppositeColor)

        holder.postOwner.setBackgroundColor(Color.parseColor(color))

        // Load the image using Glide or Picasso library
        Glide.with(holder.itemView.context)
            .load(post.imageUrl)
            .into(holder.postImage)

        holder.postDetail.text = post.postDetail
        holder.postOwner.text = post.post_owner

        if (post.post_owner == crntUserEmail){
           holder.cmntBtn.visibility = View.INVISIBLE
        }

        if (post.post_owner == crntUserEmail){
            holder.helpbtn.visibility = View.INVISIBLE
        }

        holder.cmntBtn.setOnClickListener {
         //   Log.d(ContentValues.TAG, "this is the related comment $position")
            val postOwner = post.post_owner
            val postTitle= post.postDetail
            val postID = post.post_key
            val homeFragment = HomeFragment()
            val bundle = Bundle()

            bundle.putString("postOwner", postOwner)
            bundle.putString("postTitle", postTitle)
            bundle.putString("postID", postID)
            bundle.putString("postPosi" , position.toString())

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

    interface OnChatButtonClickListener {
        fun onChatButtonClick(post: Post)
    }

    }