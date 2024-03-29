package com.example.helpinghand.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.helpinghand.Models.Post
import com.example.helpinghand.R
import com.google.firebase.database.FirebaseDatabase

class PostAdapter(private val posts: MutableList<Post> ) : RecyclerView.Adapter<PostAdapter.ViewHolder>() {
    private val colors = arrayOf("#E1BEE7","#D1C4E9", "#C5CAE9", "#BBDEFB", "#B3E5FC", "#B2EBF2", "#B2DFDB", "#C8E6C9")

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val postImage: ImageView = itemView.findViewById(R.id.post_image)
        val postDetail: TextView = itemView.findViewById(R.id.post_detail)
        val deleteButton: Button = itemView.findViewById(R.id.button3)
        val editbutton: Button = itemView.findViewById(R.id.button2)
        val postCardView : CardView = itemView.findViewById(R.id.postCardView)
        val editPost: EditText = itemView.findViewById(R.id.editpost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        val color = colors[position % colors.size]
        holder.postCardView.setBackgroundColor(Color.parseColor(color))


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

        holder.editbutton.setOnClickListener {
            holder.editPost.setVisibility(View.VISIBLE)

            val newPostDetail = holder.editPost.text.toString().trim()
            if (newPostDetail.isNotEmpty()) {
                post.postDetail = newPostDetail
                post.post_key?.let { it1 ->
                    FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("posts").child(
                        it1
                    )
                        .child("postDetail")
                        .setValue(newPostDetail)
                        .addOnSuccessListener {
                            notifyDataSetChanged()
                            Toast.makeText(
                                holder.itemView.context,
                                "Post updated successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            holder.editPost.setVisibility(View.INVISIBLE)
                            holder.editPost.setText("")
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                holder.itemView.context,
                                "Failed to update post: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            } else {
                Toast.makeText(
                    holder.itemView.context,
                    "Please enter a new post detail",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    override fun getItemCount() = posts.size
}