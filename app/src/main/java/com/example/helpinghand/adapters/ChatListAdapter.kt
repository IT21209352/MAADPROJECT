package com.example.helpinghand.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.recyclerview.widget.RecyclerView
import com.example.helpinghand.ChatFragment
import com.example.helpinghand.ChatMessagesActivity
import com.example.helpinghand.R



class ChatListAdapter:RecyclerView.Adapter<ChatListAdapter.ViewHolder>(){

    val fragmentChat = ChatFragment()

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

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.tvChatName.text = "Adam Driver"
        holder.tvRecMsg.text = "Hello! I'm Fine!"
        holder.tvMsgCount.text = "1"
        holder.tvRecTime.text = "11.04PM"

        holder.itemView.setOnClickListener { view ->
            val intent = Intent(view.context, ChatMessagesActivity::class.java)
            view.context.startActivity(intent)
        }

        holder.itemView.setOnLongClickListener { view ->

            val popupMenu = androidx.appcompat.widget.PopupMenu(view.context, holder.itemView)


            popupMenu.inflate(R.menu.popup_chat)

            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.delete -> {
                        Toast.makeText(view.context, "Chat Deleted", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> {
                        true
                    }
                }

            }
            try {
                val popup = androidx.appcompat.widget.PopupMenu::class.java.getDeclaredField("mPopup")
                popup.isAccessible = true
                val menu = popup.get(popupMenu)
                menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                    .invoke(menu,true)
            }
            catch (e: Exception)
            {
                Log.d("error", e.toString())
            }
            finally {
                popupMenu.show()
            }
            true

        }

    }



    }




