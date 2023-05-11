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
import com.example.helpinghand.Models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
//import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.random.Random


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
        val registerBtn = view.findViewById<Button>(R.id.otpSubmitBtn)
        val userEmail = view.findViewById<EditText>(R.id.otpEmailInput)
        val userName = view.findViewById<EditText>(R.id.edtTxtUserName)
        val userPassword = view.findViewById<EditText>(R.id.edtTxtUserPasswrod)
        val userPasswordRetype = view.findViewById<EditText>(R.id.edtTxtUserPassRetype)
        val progressBar = view.findViewById<ProgressBar>(R.id.registerProgressBar)

        registerBtn.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val email = userEmail.text.toString()
            val password = userPassword.text.toString()
            val psswordRetype = userPasswordRetype.text.toString()
            val name = userName.text.toString()

            if(email == "" || password =="" || psswordRetype =="" || name == ""){
                Toast.makeText(activity, "Please fill all the fields...", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
            }else{
                if(password != psswordRetype){
                    Toast.makeText(activity, "Passwords doesn't match...", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }else{
                    if (password.length<6){
                        Toast.makeText(activity, "Passwords must have more that 6 characters...", Toast.LENGTH_SHORT).show()
                        progressBar.visibility = View.GONE
                    }else{
                        if (isEmailValid(email)){
                            auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener() { task ->
                                    if (task.isSuccessful) {
                                        // Sign in success, update UI with the signed-in user's information
                                        val user = auth.currentUser
                                        val profileUpdates = UserProfileChangeRequest.Builder()
                                            .setDisplayName(name)
                                            .build()
                                        user?.updateProfile(profileUpdates)
                                            ?.addOnCompleteListener { profileTask ->
                                                if (profileTask.isSuccessful) {
                                                    Log.d(TAG, "User profile updated. ${user.displayName}")
                                                }
                                            }

                                        addUserToDatabase(name, email, user?.uid!!)

                                        Log.d(TAG, "createUserWithEmail:success")

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

                        }else  {
                            Toast.makeText(activity, "Please enter a valid email...", Toast.LENGTH_SHORT).show()
                            progressBar.visibility = View.GONE
                        }

                    }
                }
            }
        }

        return view
    }
    private fun isEmailValid(email: String): Boolean {
        val regex = """^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Z|a-z]{2,}$""".toRegex()
        return regex.matches(email)
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {

        val imageIds = arrayOf(
            R.drawable.ss2,
            R.drawable.ss3,
            R.drawable.ss4,
            R.drawable.ss5,
            R.drawable.ss6,
            R.drawable.ss7,
            R.drawable.ss8,
            R.drawable.ss9,
            R.drawable.ss10,
            R.drawable.ss11,
            R.drawable.ss12,
            R.drawable.ss13,
        )
        val selectedImageId = imageIds[Random.nextInt(imageIds.size)]

        val mDbRef = FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app").reference
        mDbRef.child("userReg").child(uid).setValue(User(email,name,uid,"Address Hasn't Updated","Phone Hasn't Updated",selectedImageId))


    }


}