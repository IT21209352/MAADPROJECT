package com.example.helpinghand.adapters

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.helpinghand.Models.CartItem
import com.example.helpinghand.Models.GlobalPostsList
import com.example.helpinghand.Models.Post
import com.example.helpinghand.Models.ShoppingCart
import com.example.helpinghand.R
import com.example.helpinghand.ViewProfile
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
        val medName: TextView = itemView.findViewById(R.id.medNameView)
        val medPrice: TextView = itemView.findViewById(R.id.medPriceView)
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

        var isadded = false

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
        holder.medName.text = post.medName
        holder.medPrice.text = "Rs " + post.medPrice.toString()

        if (post.post_owner == crntUserEmail){
           holder.cmntBtn.visibility = View.INVISIBLE
        }

        if (post.post_owner == crntUserEmail){
            holder.helpbtn.visibility = View.INVISIBLE
        }

        holder.cmntBtn.setOnClickListener {

            val cartItem = post.medPrice?.let { it1 -> CartItem("${post.medName}", it1,1) }

            if (cartItem != null && !isadded) {
                ShoppingCart.addItem(cartItem)
                Toast.makeText(context, "Medicine added to the cart", Toast.LENGTH_SHORT).show()
                isadded = true
            }else{
                Toast.makeText(context, "Medicine already in the cart", Toast.LENGTH_SHORT).show()
            }
        }

        holder.helpbtn.setOnClickListener {
            onChatButtonClickListener.onChatButtonClick(post)
        }

        holder.postOwner.setOnClickListener {
            val userID = post.post_ownerID
            val auth = FirebaseAuth.getInstance()
            val uID = auth.currentUser?.uid
            if (userID != uID){
                val proFragment = ViewProfile()
                val bundle = Bundle()

                bundle.putString("PostOwnerID", userID)
                proFragment.arguments = bundle

                val transaction = fragmentManager.beginTransaction()
                transaction.replace(R.id.fragmentContainerView, proFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }
    }

    override fun getItemCount() = posts.size

    interface OnChatButtonClickListener {
        fun onChatButtonClick(post: Post)
    }

    }