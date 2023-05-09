package com.example.helpinghand
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.helpinghand.adapters.NetworkUtils
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var navCtrl:NavController

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Inflate the layout for this fragment
        if (NetworkUtils.isNetworkAvailable(this)) {
            // Network is available, continue with app startup
        } else {
            // Network is not available, show an error message or take appropriate action
            Toast.makeText(this@MainActivity, "Please check your network connection", Toast.LENGTH_LONG).show()
        }


//        val db = Firebase.firestore
//        val user = hashMapOf(+
//            "last" to "Lovelace",
//            "born" to 1815
//        )
//
//        db.collection("users")
//            .add(user)
//            .addOnSuccessListener { documentReference ->
//                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
//                Toast.makeText(this@MainActivity, "Data Added to DB", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener { e ->
//                Log.w(TAG, "Error adding document", e)
//                Toast.makeText(this@MainActivity, "Error occurred with database", Toast.LENGTH_SHORT).show()
//            }

    }

}