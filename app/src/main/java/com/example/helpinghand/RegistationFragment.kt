package com.example.helpinghand

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RegistationFragment : Fragment() {

    //val mAuth = FirebaseAuth.getInstance()

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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


        registerBtn.setOnClickListener {
            val Email = userEmail.text.toString()
            val Password = userPassword.text.toString()
            val PsswordRetype = userPasswordRetype.text.toString()

            if(Email == "" || Password =="" || PsswordRetype =="" ){
                Toast.makeText(activity, "Please Enter a Valid Email Address", Toast.LENGTH_SHORT).show()
            }else{


                auth.createUserWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                          //  val user = mAuth.currentUser
                            Toast.makeText(activity, "Registration Successful.", Toast.LENGTH_SHORT).show()
                            val user = auth.currentUser
                            Navigation.findNavController(view).navigate(R.id.action_registationFragment_to_loginFragment)

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(activity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        return view
    }

}