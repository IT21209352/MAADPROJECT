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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class OTPFragment : Fragment() {
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth = Firebase.auth
        val auth = FirebaseAuth.getInstance()
        val view =  inflater.inflate(R.layout.fragment_o_t_p, container, false)
        val submitBtn = view.findViewById<Button>(R.id.otpSubmitBtn)
        val emailInput = view.findViewById<EditText>(R.id.otpEmailInput)

        submitBtn.setOnClickListener {
            val email = emailInput.text.toString()
            if (isEmailValid(email)){
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(activity, "Rest link has been sent. It may take some time to receive", Toast.LENGTH_LONG).show()
                            emailInput.text.clear()
                            Navigation.findNavController(view).navigate(R.id.action_OTPFragment_to_loginFragment)
                        } else {
                            Toast.makeText(activity, "Email is not registered", Toast.LENGTH_SHORT).show()
                        }
                    }
            }else{
                Toast.makeText(activity, "Email is invalid", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
    private fun isEmailValid(email: String): Boolean {
        val regex = """^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$""".toRegex()
        return regex.matches(email)
    }


}