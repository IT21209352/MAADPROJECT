package com.example.helpinghand

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
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

        logoutBtn.setOnClickListener {

            Firebase.auth.signOut()

            activity?.let {
                val intent = Intent(it,MainActivity::class.java)
                it.startActivity(intent)
            }

        }


        return view
    }

}