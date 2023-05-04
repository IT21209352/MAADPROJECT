package com.example.helpinghand.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.helpinghand.Models.Comment
import com.example.helpinghand.R

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.MyViewHolder>() {

    private val commentList = ArrayList<Comment>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.acomment_layout,
            parent,false
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
    }

    fun  updateCommentList(commentList : List<Comment>){
        this.commentList.clear()
        this.commentList.addAll(commentList)
        notifyDataSetChanged()
    }

    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val displayCommentOwner : TextView = itemView.findViewById<TextView>(R.id.display_comment_ownner)
        val displayTheComment : TextView = itemView.findViewById<TextView>(R.id.display_the_commnet)
    }


}

