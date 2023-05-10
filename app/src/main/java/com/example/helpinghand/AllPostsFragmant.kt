package com.example.helpinghand

import android.content.ContentValues.TAG
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
import com.bumptech.glide.manager.SupportRequestManagerFragment
import com.example.helpinghand.Models.GlobalPostsList
import com.example.helpinghand.Models.Post
import com.example.helpinghand.adapters.AllPostsAdaptor
import com.example.helpinghand.databinding.FragmentAllPostsFragmantBinding
import com.example.helpinghand.databinding.FragmentMyPostsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class AllPostsFragmant : Fragment(), AllPostsAdaptor.OnChatButtonClickListener {

    private lateinit var binding: FragmentAllPostsFragmantBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: DatabaseReference
    private lateinit var postAdapter: AllPostsAdaptor
    private var postList: MutableList<Post> = mutableListOf()
    private lateinit var postPosi : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        postPosi = arguments?.getString("postPosi") ?: ""

        // Inflate the layout for this fragment
        binding = FragmentAllPostsFragmantBinding.inflate(inflater, container, false)
        firebaseAuth = Firebase.auth
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase =
            FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app")
                .getReference("posts")

        postAdapter = AllPostsAdaptor(requireContext(), this, postList,requireActivity().supportFragmentManager)
        binding.allPostsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postAdapter
        }
        loadMyPosts()

        return binding.root
    }


    private fun loadMyPosts() {
        val userId = firebaseAuth.currentUser?.email; /* */

        if (userId != null) {
            firebaseDatabase
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        postList.clear()
                        for (postSnapshot in snapshot.children) {
                            val postId = postSnapshot.key.toString()
                            val post = postSnapshot.getValue(Post::class.java)

                            //   Log.d(ContentValues.TAG, "-----------------------------------this is post id $pos")
                            //    Log.d(ContentValues.TAG, "DocumentSnapshot added with ID: $commentID")

                            if (post != null) {
                                postList.add(post)
                                postAdapter.notifyDataSetChanged()

                                val rcView = binding.allPostsRecyclerView

                                if (rcView != null && postPosi != ""){
                                    //Log.d(context.toString(), "-----------------------------------------${postPosi.toInt()}")
                                    val layoutManager = rcView!!.layoutManager as LinearLayoutManager
                                    if (postPosi.toInt() != RecyclerView.NO_POSITION) {
                                        // scroll to the post if it exists in the adapter
                                        layoutManager.scrollToPositionWithOffset(postPosi.toInt(), 0)
                                    }
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            requireContext(),
                            "Failed to load posts: ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }

    override fun onChatButtonClick(post: Post) {
        val currentUserId = firebaseAuth.currentUser?.uid
        val otherUserEmail = post.post_owner


        val databaseReference = FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("userReg")

        databaseReference.orderByChild("email").equalTo(otherUserEmail).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        val otherUserId = data.child("uid").value.toString() // get the user ID
                        val otherUserName = data.child("name").value.toString() // get the user's name
                        // display
                        Log.d(TAG, "User ID: $otherUserId, Name: $otherUserName")

                        // Generate chat ID
                        val chatId = getChatId(currentUserId!!, otherUserId)

                        // Launch ChatActivity with necessary data
                        val intent = Intent(requireContext(), ChatMessagesActivity::class.java)
                        intent.putExtra("chatId", chatId)
                        intent.putExtra("otherUserName", otherUserName)
                        intent.putExtra("otherUserId", otherUserId)
                        startActivity(intent)

                    }
                } else {
                    // handle the case where no user was found with the given email
                    Toast.makeText(context, "Could not find user", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "Could not find user")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // handle any errors that occur while trying to read the data
            }
        })


    }

    private fun getChatId(userId1: String, userId2: String): String {
        return if (userId1 < userId2) {
            "$userId1-$userId2"
        } else {
            "$userId2-$userId1"
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}

