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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var navCtrl:NavController

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



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