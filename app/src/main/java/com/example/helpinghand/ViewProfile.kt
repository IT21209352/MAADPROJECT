package com.example.helpinghand

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.helpinghand.Models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class ViewProfile : Fragment() {

    private  var postOwnerID : String = arguments?.getString("PostOwnerID") ?: ""
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_profile, container, false)

        return  view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postOwnerID =  arguments?.getString("PostOwnerID") ?: ""
        Log.d(ContentValues.TAG, "------------------------------------------------------$postOwnerID")
        auth = Firebase.auth
        val proPicView = view.findViewById<ImageView>(R.id.randomProPicView2)
        val emailView = view.findViewById<TextView>(R.id.randomEmailView2)
        val nameView = view.findViewById<TextView>(R.id.randomNameView2)
        val addrsView = view.findViewById<TextView>(R.id.randomAddressView2)
        val phnView = view.findViewById<TextView>(R.id.randomPhoneView2)

        val mDbRef = FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app").reference
        val userRef = postOwnerID?.let { mDbRef.child("userReg").child(it) }

        userRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userObject = snapshot.getValue(User::class.java)
                nameView.text = userObject?.name
                emailView.text = userObject?.email
                addrsView.text = userObject?.address.toString()
                phnView.text = userObject?.phone.toString()
                Glide.with(view)
                    .load(userObject?.profilePictureResourceId)
                    .into(proPicView)

            }  override fun onCancelled(error: DatabaseError) {
                Log.d(ContentValues.TAG, "getUser:onCancelled", error.toException())
            }
        })

    }

}