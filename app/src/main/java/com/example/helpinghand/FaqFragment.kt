package com.example.helpinghand

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helpinghand.Models.FAQ
import com.example.helpinghand.adapters.faqadapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase


class FaqFragment : Fragment() {
    private lateinit var firebaseAuth:FirebaseAuth
    private lateinit var firebaseDatabase: DatabaseReference

    private  lateinit var auth:FirebaseAuth
    private lateinit var  ref:DatabaseReference
    private  lateinit var faqadapter: faqadapter
    private var faqlist:MutableList<FAQ> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_faq, container, false)

        firebaseAuth=FirebaseAuth.getInstance()
        firebaseDatabase=FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("faq")

        val btnadd=view.findViewById<Button>(R.id.btnadd)
        val  eidt= view.findViewById<EditText>(R.id.tvEidtT)
        btnadd.setOnClickListener {
            val userid = firebaseAuth.currentUser?.uid
            val faqid = firebaseDatabase.push().key
            val faqtext = eidt.text.toString()

            if (faqtext.isNotEmpty()) { // Check if the text field is not empty
                val faq = FAQ(faqid, userid, faqtext)
                faqlist.add(faq)

                if (faqid != null) {
                    firebaseDatabase.child(faqid).setValue(faq)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                eidt.setText("")
                                Toast.makeText(requireContext(), "Note added!", Toast.LENGTH_SHORT).show()
                                faqadapter.notifyDataSetChanged() // Notify adapter that the data has changed
                            } else {
                                Toast.makeText(requireContext(), "Failed to add Note", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            } else {
                Toast.makeText(requireContext(), "Please enter a Note", Toast.LENGTH_SHORT).show()
            }

        }

        auth = Firebase.auth
        auth=FirebaseAuth.getInstance()
        ref=FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("faq")
        faqadapter= faqadapter(faqlist)

        val faqRecyclerView = view.findViewById<RecyclerView>(R.id.rvFaq)
        faqRecyclerView.adapter = faqadapter
        faqRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        loardfaqs()






        // Inflate the layout for this fragment
        return view
    }

    private fun loardfaqs() {
        val uid=auth.currentUser?.uid
        if (uid!=null){

            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    faqlist.clear()
                    for (faqSnapshot in snapshot.children){
                        val faq=faqSnapshot.getValue(FAQ::class.java)
                        if (faq?.userid==uid){
                            faqlist.add(faq)
                        }
                    }
                    faqadapter.notifyDataSetChanged()

                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(),"error",Toast.LENGTH_SHORT).show()
                }

            })
        }
    }

}