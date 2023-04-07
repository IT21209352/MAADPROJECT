package com.example.helpinghand


import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ChatMessagesActivity : AppCompatActivity() {

    lateinit var btn: ImageView
    lateinit var msg: TextView
    val fragmentChat = ChatFragment()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_messages)

        btn = findViewById(R.id.imgBackBtn)
        msg = findViewById(R.id.sentMsg)
        popupDelete()

        btn.setOnClickListener(){
            finish()
        }
    }

    private fun popupDelete() {

        val popupMenu = androidx.appcompat.widget.PopupMenu(this, msg)

        popupMenu.inflate(R.menu.popup_message)

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.delete -> {
                    Toast.makeText(this, "Message Deleted", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.edit -> {
                    Toast.makeText(this, "Message Edited", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> {
                    true
                }
            }

        }

        // event on long press on image
        msg.setOnLongClickListener {
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