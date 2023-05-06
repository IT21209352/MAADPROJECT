package com.example.helpinghand.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.helpinghand.Models.Post
import com.example.helpinghand.R

class AllPostsAdaptor(private val posts: MutableList<Post> ) : RecyclerView.Adapter<AllPostsAdaptor.ViewHolder>() {
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
        val post = posts[position]
        val color = colors[position % colors.size]
        holder.postCardView.setBackgroundColor(Color.parseColor(color))

        // Load the image using Glide or Picasso library
        Glide.with(holder.itemView.context)
            .load(post.imageUrl)
            .into(holder.postImage)

        holder.postDetail.text = post.postDetail

        holder.helpbtn.setOnClickListener {
//            post.post_key?.let { it1 ->
//                FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("posts").child(
//                    it1
//                )
//                    .removeValue()
//                    .addOnSuccessListener {
//                        // Remove the post from the postList and update the adapter
//                        posts.remove(post)
//                        notifyDataSetChanged()
//
//                        Toast.makeText(
//                            holder.itemView.context,
//                            "Post deleted successfully",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                    .addOnFailureListener { e ->
//                        Toast.makeText(
//                            holder.itemView.context,
//                            "Failed to delete post: ${e.message}",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//            }
        }

    }

    override fun getItemCount() = posts.size
}