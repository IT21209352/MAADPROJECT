package com.example.helpinghand.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.helpinghand.R

class ChatListAdapter:RecyclerView.Adapter<ChatListAdapter.ViewHolder>(){

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){

        val imgUser: ImageView
        val tvChatName: TextView
        val tvRecMsg: TextView
        val tvRecTime: TextView
        val tvMsgCount: TextView

        init {
            imgUser = view.findViewById(R.id.imgUser)
            tvChatName = view.findViewById(R.id.tvChatName)
            tvRecMsg = view.findViewById(R.id.tvRecMsg)
            tvMsgCount = view.findViewById(R.id.tvMsgCount)
            tvRecTime = view.findViewById(R.id.tvRecTime)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder{
        val view = LayoutInflater.from(parent.context).
        inflate(R.layout.chat_list,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvChatName.text = "Adam Driver"
        holder.tvRecMsg.text = "Hello! I'm Fine!"
        holder.tvMsgCount.text = "1"
        holder.tvRecTime.text = "11.11PM"
//        holder.itemView.setOnClickListener()
    }

    override fun getItemCount(): Int {
        return 1
    }





}