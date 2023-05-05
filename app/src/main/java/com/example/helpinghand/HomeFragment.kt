package com.example.helpinghand
import android.content.AbstractThreadedSyncAdapter
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helpinghand.Models.Comment
import com.example.helpinghand.Models.CommentViewModel
import com.example.helpinghand.adapters.CommentAdapter
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
    lateinit var adapter: CommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        auth = Firebase.auth
        val logoutBtn = view.findViewById<Button>(R.id.main_logout_btn)
        val cmntBtn= view.findViewById<Button>(R.id.cmntBtn)

        logoutBtn.setOnClickListener {

            Firebase.auth.signOut()

            activity?.let {
                val intent = Intent(it,MainActivity::class.java)
                it.startActivity(intent)
            }

        }

        cmntBtn.setOnClickListener {

            val cmntInput = view.findViewById<EditText>(R.id.cmntInput)
            val texts: String = cmntInput.getText().toString()
            db = FirebaseFirestore.getInstance();
            textCollectionRef = db!!.collection("comments")
            val user =  auth.currentUser?.email.toString()

            if (texts.isNotEmpty()) {
                val textMap = hashMapOf(
                    "comments_comment" to texts,
                    "comments_owner" to user,
                    "comment_id" to ""
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
                            val textMap2 = hashMapOf(
                                "comments_comment" to texts,
                                "comments_owner" to user,
                                "comment_id" to commentID
                            )
                            firebaseDatabase.child("post_comments").child(commentID).setValue(textMap2)
                            cmntInput.setText("")
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(activity, "Error Occurred", Toast.LENGTH_SHORT).show()
                            Log.w(TAG, "Error adding document", e)
                        }
                }
            }
        }


        firebaseDatabase = FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("comments")
        firebaseDatabase.child("post_comments").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the existing comments list
                viewModel.allComments

                // Iterate over the snapshot children and add each comment to the ViewModel
                for (postSnapshot in snapshot.children) {
                    Log.w(TAG, "comment key is ${postSnapshot.key}")
                    val comment = postSnapshot.getValue(Comment::class.java)

                    if (comment != null) {
                        if (comment.comment_id ==""){
//                            val textMap = hashMapOf(
//                                "comment_id" to postSnapshot.key
//                            )
//                            firebaseDatabase.setValue(textMap)
                        }
                    }

                }

            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        return view
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


    }
}