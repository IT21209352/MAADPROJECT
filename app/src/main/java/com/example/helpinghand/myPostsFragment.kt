package com.example.helpinghand

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.helpinghand.Models.Comment
import com.example.helpinghand.Models.Post
import com.example.helpinghand.adapters.PostAdapter
import com.example.helpinghand.databinding.FragmentMyPostsBinding
import com.example.helpinghand.databinding.FragmentPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class myPostsFragment : Fragment() {


    private lateinit var binding: FragmentMyPostsBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: DatabaseReference
    private lateinit var postAdapter: PostAdapter
    private var postList: MutableList<Post> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyPostsBinding.inflate(inflater, container, false)
        firebaseAuth = Firebase.auth
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("posts")
        postAdapter = PostAdapter(postList)
        binding.recyclerView.apply {
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
                                if (post.post_owner==userId){
                                    postList.add(post)
                                    postAdapter.notifyDataSetChanged()
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
}