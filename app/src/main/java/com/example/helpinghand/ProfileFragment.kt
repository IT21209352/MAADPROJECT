package com.example.helpinghand
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.helpinghand.Models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val logoutBtn = view?.findViewById<Button>(R.id.main_logout_btn)

        auth = Firebase.auth
        if (logoutBtn != null) {
            logoutBtn.setOnClickListener {
                Firebase.auth.signOut()
                activity?.let {
                    val intent = Intent(it, MainActivity::class.java)
                    it.startActivity(intent)
                }
            }
        }

        val userEmail = auth.currentUser?.email.toString()
        val userID = auth.currentUser?.uid

        val emailView = view.findViewById<TextView>(R.id.profileEmailView)
        val nameView = view.findViewById<TextView>(R.id.profileNameView)
        val addrsView = view.findViewById<TextView>(R.id.profileAddressView)
        val phnView = view.findViewById<TextView>(R.id.profilePhoneView)

        val mDbRef = FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app").reference
        val userRef = userID?.let { mDbRef.child("userReg").child(it) }

        userRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userObject = snapshot.getValue(User::class.java)
               // Log.d(ContentValues.TAG, "-----------------------------------------------------------------${userObject?.name}")


                nameView.text = userObject?.name
                emailView.text = userObject?.email
                addrsView.text = userObject?.address.toString()
                phnView.text = userObject?.phone.toString()


            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(ContentValues.TAG, "getUser:onCancelled", error.toException())
            }
        })

    }
}