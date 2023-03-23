package com.example.helpinghand

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_login, container, false)
        // Inflate the layout for this fragment
         return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        auth = Firebase.auth

        val loginButton = view.findViewById<Button>(R.id.button_register)
        val signupButton = view.findViewById<Button>(R.id.button_signup)
        val passrestButton = view.findViewById<TextView>(R.id.frogot_password_link)
        val loginEmail = view.findViewById<EditText>(R.id.edtTxtLoginUserEmail)
        val loginPassword = view.findViewById<EditText>(R.id.editTxtLoginPassword)

        loginButton.setOnClickListener {

            val email = loginEmail.text.toString()
            val password = loginPassword.text.toString()

            if(email == "" || password ==""  ){
                Toast.makeText(activity, "Please Enter a Valid Email Address", Toast.LENGTH_SHORT).show()
            }else{

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            Toast.makeText(activity, "Login Successful", Toast.LENGTH_SHORT).show()
                            activity?.let {
                                val intent = Intent(it,MainActivity2::class.java)
                                it.startActivity(intent)
                            }

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(activity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()

                        }
                    }
            }
        }



        signupButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registationFragment)
        }

        passrestButton.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_OTPFragment)
        }


        super.onViewCreated(view, savedInstanceState)
    }



}

