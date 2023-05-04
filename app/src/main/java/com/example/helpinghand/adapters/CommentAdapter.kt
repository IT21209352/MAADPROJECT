package com.example.helpinghand.adapters

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.helpinghand.Models.Comment
import com.example.helpinghand.R
import com.google.firebase.database.FirebaseDatabase



class CommentAdapter : RecyclerView.Adapter<CommentAdapter.MyViewHolder>() {

    private val commentList = ArrayList<Comment>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.acomment_layout,
            parent,false,

        )
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
       return commentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = commentList[position]

        holder.displayCommentOwner.text = currentitem.comments_owner
        holder.displayTheComment.text = currentitem.comments_comment

        holder.bind(currentitem)
    }

    fun  updateCommentList(commentList : List<Comment>){
        this.commentList.clear()
        this.commentList.addAll(commentList)
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        private val cmntDeleteButton: Button = itemView.findViewById(R.id.cmntDeleteBtn)
        val displayCommentOwner : TextView = itemView.findViewById<TextView>(R.id.display_comment_ownner)
        val displayTheComment : TextView = itemView.findViewById<TextView>(R.id.display_the_commnet)

        fun bind(comment: Comment) {
            cmntDeleteButton.setOnClickListener {

                Log.d(TAG, "This is the comment to be deleted $comment")
                if (comment != null) {
                    deleteComment(comment)
                }
            }
        }

        private fun deleteComment(comment: Comment) {
            val theID = comment.comment_id
            val firebaseDatabase = FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app")
            val commentRef =
                theID?.let {
                    firebaseDatabase.getReference("comments").child("post_comments").child(
                        it
                    )
                }
            Log.d(TAG, "Delete comment contacted this is the reference id $theID")
            commentRef?.removeValue()?.addOnSuccessListener {
                Log.d(TAG, "Comment deleted")
            }?.addOnFailureListener { e ->
                Log.w(TAG, "Error deleting comment", e)
            }
        }
    }
}

