package com.example.helpinghand

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helpinghand.adapters.ChatListAdapter

class ChatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_chatList)
        val adapter = ChatListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)


//        popupMenu()


    }

//    private fun popupMenu() {
//        // creating a object of Popupmenu
//        val popupMenu = androidx.appcompat.widget.PopupMenu(this,)
//
//        // we need to inflate the object
//        // with popup_menu.xml file
//        popupMenu.inflate(R.menu.popup_chat)
//
//        // adding click listener to image
//        popupMenu.setOnMenuItemClickListener {
//            when (it.itemId) {
//                R.id.delete -> {
//                    Toast.makeText(activity, "Deleted", Toast.LENGTH_SHORT).show()
//                    true
//                }
//                else -> {
//                    true
//                }
//            }
//
//        }
//
//        // event on long press on image
//        recyclerView.setOnLongClickListener {
//            try {
//                val popup = androidx.appcompat.widget.PopupMenu::class.java.getDeclaredField("mPopup")
//                popup.isAccessible = true
//                val menu = popup.get(popupMenu)
//                menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
//                    .invoke(menu,true)
//            }
//            catch (e: Exception)
//            {
//                Log.d("error", e.toString())
//            }
//            finally {
//                popupMenu.show()
//            }
//            true
//        }
//    }

}