package com.example.helpinghand.adapters
import android.app.Activity
import android.content.ContentValues.TAG
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.helpinghand.Models.Comment
import com.example.helpinghand.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.MyViewHolder>() {

    private val commentList = ArrayList<Comment>()
    private val colors = arrayOf("#FFCDD2", "#F8BBD0", "#E1BEE7", "#D1C4E9", "#C5CAE9", "#BBDEFB", "#B3E5FC", "#B2EBF2", "#B2DFDB", "#C8E6C9")

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
        val activity = holder.itemView.context as Activity

        val color = colors[position % colors.size]
        holder. cmntCArdView.setBackgroundColor(Color.parseColor(color))

        holder.bind(currentitem,activity)
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
        val cmntCArdView : CardView = itemView.findViewById(R.id.cmntCardView)

        fun bind(comment: Comment,activity: Activity) {

            var auth: FirebaseAuth = Firebase.auth
            val crntUserEmail = auth.currentUser?.email.toString()

            if (comment.comments_owner != crntUserEmail){
                cmntDeleteButton.visibility = View.INVISIBLE
                Log.d(TAG, "This is the comment to be deleted $comment")
            }


            cmntDeleteButton.setOnClickListener {
                if (comment.comments_owner == crntUserEmail){

                    Log.d(TAG, "This is the comment to be deleted $comment")
                    deleteComment(comment)

                }else{

                    Log.d(TAG, "Owner validation failed")
                    Toast.makeText(activity , "You can not delete others comments...", Toast.LENGTH_SHORT).show()
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

