package com.example.helpinghand

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helpinghand.Models.ChatList
import com.example.helpinghand.Models.Messages
import com.example.helpinghand.adapters.ChatListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class ChatFragment : Fragment() {

    lateinit var chatListAdapter: ChatListAdapter
    private lateinit var currentUserId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        chatListAdapter = ChatListAdapter(requireContext())
        currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val chatListRecyclerView = view.findViewById<RecyclerView>(R.id.recycler_chatList)
        chatListRecyclerView.adapter = chatListAdapter
        chatListRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val filterEditText = view.findViewById<EditText>(R.id.edtSearch)

        filterEditText.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Call fetchChats() with the updated search query
                fetchChats(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun afterTextChanged(s: Editable?) {}
        })


        val messagesRef =
            FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("messagesZ")
        messagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called every time there's a change in the chats node
                // Load the updated chats data here
                fetchChats()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors here
            }
        })


        //fetchChats()

        return view
    }

    override fun onResume() {
        super.onResume()

        // Refresh the data here
        fetchChats()
    }

    private fun fetchChats(searchQuery: String = "") {
        val messagesRef =
            FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("messagesZ")
        val usersRef =
            FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("userReg")
        val query = messagesRef.orderByChild("time")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Group messages by chat ID
                    val chatMessagesMap = mutableMapOf<String, MutableList<Messages>>()
                    for (messageSnapshot in dataSnapshot.children) {
                        val message = messageSnapshot.getValue(Messages::class.java)
                        if (message != null && (message.senderId == currentUserId || message.receiverId == currentUserId)) {
                            val chatId = getChatId(
                                currentUserId,
                                if (message.senderId!! == currentUserId) message.receiverId!! else message.senderId
                            )
                            if (!chatMessagesMap.containsKey(chatId)) {
                                chatMessagesMap[chatId] = mutableListOf()
                            }
                            chatMessagesMap[chatId]?.add(message)
                        }
                    }
                    // Map chat messages to chat objects
                    val chatList = mutableListOf<ChatList>()
                    val userIds = chatMessagesMap.keys.flatMap { chatId ->
                        val otherUserId = getOtherUserId(chatId, currentUserId)
                        listOf(otherUserId, currentUserId)
                    }.toSet()
                    val usernamesMap = mutableMapOf<String, String>()
                    usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(usersSnapshot: DataSnapshot) {
                            for (userId in userIds) {
                                val username = usersSnapshot.child(userId).child("name")
                                    .getValue(String::class.java)
                                if (username != null) {
                                    usernamesMap[userId] = username
                                }
                            }
                            for ((chatId, chatMessages) in chatMessagesMap) {
                                val otherUserId = getOtherUserId(chatId, currentUserId)
                                val otherUsername = usernamesMap[otherUserId] ?: "Unknown"
                                val lastMessage = chatMessages.lastOrNull()
                                val lastMessageText = if (lastMessage?.senderId == currentUserId) {
                                    "You: ${lastMessage.message}"
                                } else {
                                    "${lastMessage?.message}"
                                }
                                val unreadCount = chatMessages.count { message ->
                                    message.receiverId == currentUserId && !message.isRead
                                }
                                val chat = ChatList(
                                    chatId,
                                    otherUserId,
                                    otherUsername,
                                    lastMessageText,
                                    lastMessage?.time,
                                    unreadCount
                                )
//                                chatList.add(chat)
                                // Filter chatList by searchQuery if it is not empty
                                if (searchQuery.isBlank() || otherUsername.contains(
                                        searchQuery,
                                        true
                                    )
                                ) {
                                    chatList.add(chat)
                                }
                            }
                            // Sort chats by last message time
                            chatList.sortByDescending { it.lastMessageTime }
                            // Update adapter
                            chatListAdapter.updateChatList(chatList)
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle the error
                            Log.e(TAG, "Error getting usernames")
                        }
                    })
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle the error
                    Log.e(TAG, "Error getting chat messages")
                }
            })
    }


    private fun getChatId(currentUserId: String, otherUserId: String): String {
        return if (currentUserId < otherUserId) {
            "$currentUserId-$otherUserId"
        } else {
            "$otherUserId-$currentUserId"
        }
    }


    private fun getOtherUserId(chatId: String, currentUserId: String): String {
        return chatId.split("-").filter { it != currentUserId }[0]

    }


}