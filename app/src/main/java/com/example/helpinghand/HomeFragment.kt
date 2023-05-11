package com.example.helpinghand
import android.content.AbstractThreadedSyncAdapter
import android.content.ContentValues.TAG
import android.content.Intent
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helpinghand.Models.Comment
import com.example.helpinghand.Models.CommentViewModel
import com.example.helpinghand.Models.Post
import com.example.helpinghand.adapters.CommentAdapter
import com.example.helpinghand.adapters.PostAdapter
import com.example.helpinghand.databinding.FragmentMyPostsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var textCollectionRef: CollectionReference? = null
    private lateinit var user: FirebaseUser
    private lateinit var firebaseDatabase: DatabaseReference
    private lateinit var viewModel : CommentViewModel
    private lateinit var commentRecyclerView : RecyclerView
    private lateinit var postRecyclerView : RecyclerView
    lateinit var adapter: CommentAdapter
    private lateinit var binding: FragmentMyPostsBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var postAdapter: PostAdapter
    private var postList: MutableList<Post> = mutableListOf()
    private lateinit var postData : HashMap<String,String>
    private lateinit var postID : String
    private lateinit var postOwner : String
    private lateinit var postTitle: String
    private lateinit var postPosi: String
    private lateinit var postOwnerID : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val cmntBtn= view.findViewById<ImageButton>(R.id.cmntBtn)

        auth = Firebase.auth

        dataLoaders()

        cmntBtn.setOnClickListener {

            val cmntInput = view.findViewById<EditText>(R.id.cmntInput)
            val texts: String = cmntInput.getText().toString()
            db = FirebaseFirestore.getInstance();
            textCollectionRef = db!!.collection("comments")
            val user =  auth.currentUser?.email.toString()

//
//            var comments_comment : String ?=null ,
//            var comments_owner : String ?=null,
//            var comment_id : String ?= null,
//            val postID : String ?= null,
//            val postOwner : String ?= null,
//            val postTitle : String ?= null,

            if (texts.isNotEmpty()) {
                val textMap = hashMapOf(
                    "comments_comment" to texts,
                    "comments_owner" to user,
                    "comment_id" to "",
                    "postID" to postID,
                    "postOwner" to postOwner,
                    "postTitle" to postTitle,
                    "postPosi" to postPosi
                )

                val firebaseDatabase = FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference("comments")
                val commentID = firebaseDatabase.child("post_comments").push().key
                Log.d(TAG, "DocumentSnapshot added with ID: $commentID")
                if (commentID != null) {
                    firebaseDatabase.child("post_comments").child(commentID)
                        .setValue(textMap)
                        .addOnSuccessListener {
                            Toast.makeText(activity, "Comment has been saved...", Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "DocumentSnapshot added with ID: $commentID")
                            val lks = 0
                            val textMap2 = hashMapOf(
                                "comments_comment" to texts,
                                "comments_owner" to user,
                                "comment_id" to commentID,
                                "postID" to postID,
                                "postOwner" to postOwner,
                                "postTitle" to postTitle,
                                "postPosi" to postPosi,
                                "likes" to lks,
                                "postOwnerID" to postOwnerID
                            )
                            firebaseDatabase.child("post_comments").child(commentID).setValue(textMap2)
                            cmntInput.setText("")
                            val fragmentTransaction = parentFragmentManager.beginTransaction()
                            fragmentTransaction.detach(this).attach(this).commit()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(activity, "Error Occurred", Toast.LENGTH_SHORT).show()
                            Log.w(TAG, "Error adding document", e)
                        }
                }
            }
        }

        return view
    }

    private fun loadMyPosts() {
        //new code
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

    private fun dataLoaders(){
        firebaseDatabase = FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("comments")
        firebaseDatabase.child("post_comments").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the existing comments list
                // Iterate over the snapshot children and add each comment to the ViewModel
                for (postSnapshot in snapshot.children) {
                    val comment = postSnapshot.getValue(Comment::class.java)
//                    val users =  auth.currentUser?.email.toString()
//                    Log.w(TAG, "--------------------------user is $users")
//                    if (comment != null) {
//                        if (comment.comments_owner == users){
//                            viewModel.allComments
//                        }
//                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        commentRecyclerView = view.findViewById(R.id.cmntRecyclerView)
        commentRecyclerView.layoutManager = LinearLayoutManager(context)
        commentRecyclerView.setHasFixedSize(true)
        adapter = CommentAdapter()
        commentRecyclerView.adapter = adapter
        viewModel = ViewModelProvider(this).get(CommentViewModel::class.java)

        viewModel.allComments.observe(viewLifecycleOwner, Observer {
            adapter.updateCommentList(it)
        })

         postTitle = arguments?.getString("postTitle") ?: ""
         postOwner =     arguments?.getString("postOwner") ?: ""
         postID = arguments?.getString("postID") ?: ""
        postPosi = arguments?.getString("postPosi") ?: ""
        postOwnerID =  arguments?.getString("postOwnerID") ?: ""
    }
}
