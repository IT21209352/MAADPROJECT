package com.example.helpinghand

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var user:FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
         return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        auth = Firebase.auth
        val user1 = Firebase.auth.currentUser

        if (user1 != null){
            Toast.makeText(activity, "You have already logged im...", Toast.LENGTH_SHORT).show()
            activity?.let {
                val intent = Intent(it,MainActivity2::class.java)
                it.startActivity(intent)
            }
        }

        val loginButton = view.findViewById<Button>(R.id.button_register)
        val signupButton = view.findViewById<Button>(R.id.button_signup)
        val passrestButton = view.findViewById<TextView>(R.id.frogot_password_link)
        val loginEmail = view.findViewById<EditText>(R.id.edtTxtLoginUserEmail)
        val loginPassword = view.findViewById<EditText>(R.id.editTxtLoginPassword)
        val progressBar = view.findViewById<ProgressBar>(R.id.loginProgressBar)
        val currentUser = auth.currentUser

        loginButton.setOnClickListener {

            progressBar.visibility = View.VISIBLE
            val email = loginEmail.text.toString()
            val password = loginPassword.text.toString()

            if(email == "" || password ==""  ){
                Toast.makeText(activity, "Please Enter a Valid Email Address", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }else{

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener{ task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            Toast.makeText(activity, "Login Successful", Toast.LENGTH_SHORT).show()
                            activity?.let {
                                val intent = Intent(it,MainActivity2::class.java)
                                it.startActivity(intent)
                            }
                            progressBar.visibility = View.GONE
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(activity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                            progressBar.visibility = View.GONE
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

