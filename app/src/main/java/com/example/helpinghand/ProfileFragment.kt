package com.example.helpinghand
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
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
        auth = Firebase.auth
        val logoutBtn = view?.findViewById<Button>(R.id.main_logout_btn)
        val edtProfile = view?.findViewById<Button>(R.id.btnEditProfile)
        val dltProfileBtn = view?.findViewById<Button>(R.id.btnDeleteProfile)
        val userID = auth.currentUser?.uid
        val proPicView = view.findViewById<ImageView>(R.id.profilePicView)
        val emailView = view.findViewById<TextView>(R.id.randomEmailView)
        val nameView = view.findViewById<TextView>(R.id.randomNameView)
        val addrsView = view.findViewById<TextView>(R.id.randomAddressView)
        val phnView = view.findViewById<TextView>(R.id.randomPhoneView)
        var resID : Int
        val btnFaq = view.findViewById<Button>(R.id.btnFaq)

        btnFaq.setOnClickListener {
            val fragment: FaqFragment = FaqFragment()
            val fragmentManager: FragmentManager = requireActivity().supportFragmentManager
            val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        if (logoutBtn != null) {
            logoutBtn.setOnClickListener {
                Firebase.auth.signOut()
                activity?.let {
                    val intent = Intent(it, MainActivity::class.java)
                    it.startActivity(intent)
                }
            }
        }


        val mDbRef = FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app").reference
        val userRef = userID?.let { mDbRef.child("userReg").child(it) }

        fun refreshProfileData() {
            val mDbRef = FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app").reference
            val userRef = auth.currentUser?.uid?.let { mDbRef.child("userReg").child(it) }

            userRef?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userObject = snapshot.getValue(User::class.java)
                    nameView.text = userObject?.name
                    emailView.text = userObject?.email
                    addrsView.text = userObject?.address.toString()
                    phnView.text = userObject?.phone.toString()
                    resID = userObject?.profilePictureResourceId!!

                    if (userObject != null) {
                        Log.d(ContentValues.TAG, "-------------------------------------------------------------------${userObject.profilePictureResourceId}")
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d(ContentValues.TAG, "getUser:onCancelled", error.toException())
                }
            }
            )
        }

        userRef?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userObject = snapshot.getValue(User::class.java)
               // Log.d(ContentValues.TAG, "-----------------------------------------------------------------${userObject?.name}")
                nameView.text = userObject?.name
                emailView.text = userObject?.email
                addrsView.text = userObject?.address.toString()
                phnView.text = userObject?.phone.toString()
                Glide.with(view)
                    .load(userObject?.profilePictureResourceId)
                    .into(proPicView)

            }
            override fun onCancelled(error: DatabaseError) {
                Log.d(ContentValues.TAG, "getUser:onCancelled", error.toException())
            }
        })

        if (edtProfile != null) {

            edtProfile.setOnClickListener {
                val updtName = view.findViewById<EditText>(R.id.profileNameUpdateInput)
                val updtAddrs = view.findViewById<EditText>(R.id.profileAddUpdateInput)
                val updtPhone = view.findViewById<EditText>(R.id.profilePhoneUpdateInput)

                val name = updtName.text.toString()
                val addrs = updtAddrs.text.toString()
                val phone = updtPhone.text.toString()

                if (name ==""){
                    Toast.makeText(activity, "User name can not be empty...", Toast.LENGTH_LONG).show()
                }else{
                    if (isPhoneNumber(phone)){
                    //    Log.d(ContentValues.TAG, "--------------------------------------$name , $addrs, $phone")

                        val textMap2 = hashMapOf(
                            "address" to addrs,
                            "email" to auth.currentUser?.email.toString() ,
                            "name" to name,
                            "phone" to phone,
                            "uid" to auth.currentUser?.uid.toString(),
                        )

                        if (userRef != null) {
                            userRef.updateChildren(textMap2 as Map<String, Any>).addOnSuccessListener{
                                Toast.makeText(activity, "Profile Updated", Toast.LENGTH_LONG).show()
                                updtName.text.clear()
                                updtAddrs.text.clear()
                                updtPhone.text.clear()
                                refreshProfileData()
                            }.addOnFailureListener{
                                Toast.makeText(activity, "Could not update the profile.Try Again", Toast.LENGTH_LONG).show()
                            }
                        }
                    }else{
                        Toast.makeText(activity, "Phone number is invalid...", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        if (dltProfileBtn != null) {
            dltProfileBtn.setOnClickListener {
                val mAuth = FirebaseAuth.getInstance()
                val user = mAuth.currentUser

                AlertDialog.Builder(requireContext())
                    .setMessage("Are you sure you want to delete your account?")
                    .setPositiveButton("Yes") { _, _ ->
                        user?.delete()
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    if (userRef != null) {
                                        userRef.removeValue().addOnSuccessListener {
                                            Toast.makeText(activity, "Account has been deleted", Toast.LENGTH_LONG).show()
                                            activity?.let {
                                                val intent = Intent(it, MainActivity::class.java)
                                                it.startActivity(intent)
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(activity, "Failed to delete account", Toast.LENGTH_LONG).show()
                                }
                            }
                    }
                    .setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    fun isPhoneNumber(phoneNumber: String): Boolean {
        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return true
        }
        val regex = """^\d{10}$""".toRegex()
        return regex.matches(phoneNumber)
    }


}