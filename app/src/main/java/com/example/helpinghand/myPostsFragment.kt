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
import com.google.firebase.database.*


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
                            //Log.d(ContentValues.TAG, "----------------1111111111111111111111111111------------------this is the post $postSnapshot")
                            val postId = postSnapshot.key.toString()
                            val post = postSnapshot.getValue(Post::class.java)

                            if (post != null) {
                             //   Log.d(ContentValues.TAG, "----------------222222222222222222222------------------this is the post $post")
                                postList.add(post)
                                postAdapter.notifyDataSetChanged()
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