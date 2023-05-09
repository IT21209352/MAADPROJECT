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

        val logoutBtn = view?.findViewById<Button>(R.id.main_logout_btn)

        auth = Firebase.auth
        if (logoutBtn != null) {
            logoutBtn.setOnClickListener {
                Firebase.auth.signOut()
                activity?.let {
                    val intent = Intent(it, MainActivity::class.java)
                    it.startActivity(intent)
                }
            }
        }


        return view
    }


}