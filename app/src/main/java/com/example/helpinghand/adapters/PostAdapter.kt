package com.example.helpinghand.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.helpinghand.Models.Post
import com.example.helpinghand.R
import com.google.firebase.database.FirebaseDatabase

class PostAdapter(private val posts: MutableList<Post> ) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postImage: ImageView = itemView.findViewById(R.id.post_image)
        val postDetail: TextView = itemView.findViewById(R.id.post_detail)
        val deleteButton: Button = itemView.findViewById(R.id.button3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]

        // Load the image using Glide or Picasso library
        Glide.with(holder.itemView.context)
            .load(post.imageUrl)
            .into(holder.postImage)

        holder.postDetail.text = post.postDetail

        holder.deleteButton.setOnClickListener {
            post.post_key?.let { it1 ->
                FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("posts").child(
                    it1
                )
                    .removeValue()
                    .addOnSuccessListener {
                        // Remove the post from the postList and update the adapter
                        posts.remove(post)
                        notifyDataSetChanged()

                        Toast.makeText(
                            holder.itemView.context,
                            "Post deleted successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            holder.itemView.context,
                            "Failed to delete post: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }

    }
    
    override fun getItemCount() = posts.size
}