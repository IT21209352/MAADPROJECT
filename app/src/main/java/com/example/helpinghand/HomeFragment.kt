package com.example.helpinghand

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var textCollectionRef: CollectionReference? = null
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

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

            Toast.makeText(activity, user, Toast.LENGTH_SHORT).show()

            val textMap = hashMapOf(
                "comments-comment" to texts,
                "comments-owner" to user

            )
            textCollectionRef!!
                .add(textMap)

                .addOnSuccessListener { documentReference ->
                    Toast.makeText(activity, "Comment has been saved...", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    cmntInput.setText("")
                }
                .addOnFailureListener { e ->
                    Toast.makeText(activity, "Error Occurred", Toast.LENGTH_SHORT).show()
                    Log.w(TAG, "Error adding document", e)
                }
        }

        return view
    }

}