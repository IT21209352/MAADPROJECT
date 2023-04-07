package com.example.helpinghand

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RegistationFragment : Fragment() {

    //val mAuth = FirebaseAuth.getInstance()

    private lateinit var auth: FirebaseAuth
    //private lateinit var prgsBar:ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
    }
        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        auth = Firebase.auth
        val view = inflater.inflate(R.layout.fragment_registation, container, false)
        val registerBtn = view.findViewById<Button>(R.id.button_register)
        val userEmail = view.findViewById<EditText>(R.id.edtTxtUserEmail)
        val userPassword = view.findViewById<EditText>(R.id.edtTxtUserPasswrod)
        val userPasswordRetype = view.findViewById<EditText>(R.id.edtTxtUserPassRetype)
            val progressBar = view.findViewById<ProgressBar>(R.id.registerProgressBar)

        registerBtn.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val email = userEmail.text.toString()
            val password = userPassword.text.toString()
            val psswordRetype = userPasswordRetype.text.toString()

            if(email == "" || password =="" || psswordRetype =="" ){
                Toast.makeText(activity, "Please fill all the fields...", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }else{
                if(password != psswordRetype){
                    Toast.makeText(activity, "Passwords doesn't match...", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }else{
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener() { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success")
                                val user = auth.currentUser
                                Toast.makeText(
                                    activity, "Registered successfully...",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Navigation.findNavController(view).navigate(R.id.action_registationFragment_to_loginFragment)
                                progressBar.visibility = View.GONE

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(
                                    activity, "Registation failed...",
                                    Toast.LENGTH_SHORT
                                ).show()
                                progressBar.visibility = View.GONE

                            }
                        }

                }
            }
        }

        return view
    }



}