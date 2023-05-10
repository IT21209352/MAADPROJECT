package com.example.helpinghand.adapters
import android.app.Activity
import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helpinghand.AllPostsFragmant
import com.example.helpinghand.Models.Comment
import com.example.helpinghand.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class CommentAdapter: RecyclerView.Adapter<CommentAdapter.MyViewHolder>() {

    private val commentList = ArrayList<Comment>()
    private val colors = arrayOf("#E1BEE7", "#D1C4E9", "#C5CAE9", "#BBDEFB", "#B3E5FC", "#B2EBF2", "#B2DFDB", "#C8E6C9")

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
        val oppositeColor = ColorUtils.blendARGB(Color.parseColor(color), Color.WHITE, 0.5f)

        holder.displayCommentOwner.setBackgroundColor(Color.parseColor(color))

        holder. cmntCArdView.setBackgroundColor(oppositeColor)

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
        val editBtn: Button = itemView.findViewById(R.id.saveUpdatedCmntBtn)
        val cmntLikeBtn : Button = itemView.findViewById(R.id.CmntlikeBtn)
        val updtCmntInput : EditText = itemView.findViewById(R.id.updateCommentInput)

        fun bind(comment: Comment,activity: Activity) {

            var auth: FirebaseAuth = Firebase.auth
            val crntUserEmail = auth.currentUser?.email.toString()

            if (comment.comments_owner != crntUserEmail){
                cmntDeleteButton.visibility = View.INVISIBLE
                editBtn.visibility = View.INVISIBLE
                cmntLikeBtn.visibility = View.VISIBLE
                updtCmntInput.visibility = View.INVISIBLE
               // Log.d(TAG, "This is the comment to be deleted $comment")
            }else{
                cmntLikeBtn.visibility = View.INVISIBLE
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

            editBtn.setOnClickListener {
                val updtText = updtCmntInput.text.toString()

                if (updtText!=null && updtText !=""){
                    val textMap2 = hashMapOf(
                        "comments_comment" to  updtText,
                        "comments_owner" to comment.comments_owner,
                        "comment_id" to comment.comment_id
                    )
                    val firebaseDatabase = comment.comment_id?.let { it1 ->
                        FirebaseDatabase
                            .getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app")
                            .getReference("comments").child("post_comments")
                            .child(it1).setValue(textMap2)
                    }
                    updtCmntInput.text.clear()
                    Toast.makeText(activity , "Comment updated...", Toast.LENGTH_SHORT).show()

                }else{
                    Toast.makeText(activity , "You must enter something to update...", Toast.LENGTH_SHORT).show()
                }
            }

            displayCommentOwner.setOnClickListener {
                val fragmentManager = (itemView.context as FragmentActivity).supportFragmentManager
                navToPost(comment,fragmentManager)
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

        private fun navToPost(comment: Comment, fragmentManager : FragmentManager){
            val postPosi = comment.postPosi
            val postFragment = AllPostsFragmant()
            val bundle = Bundle()
           // Log.d(TAG, "-------------------------------------------------------- $postPosi")
            bundle.putString("postPosi", postPosi)
            postFragment.arguments = bundle

            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainerView, postFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }


}

