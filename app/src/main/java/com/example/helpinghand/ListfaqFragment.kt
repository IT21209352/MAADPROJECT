package com.example.helpinghand

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.helpinghand.Models.FAQ
import com.example.helpinghand.adapters.faqadapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class ListfaqFragment : Fragment() {

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
        // Inflate the layout for this fragment
        val   View= inflater.inflate(R.layout.fragment_listfaq, container, false)

        auth = Firebase.auth
        auth=FirebaseAuth.getInstance()
        ref=FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("faq")
        faqadapter= faqadapter(faqlist)

        val faqRecyclerView = View.findViewById<RecyclerView>(R.id.recycler_faq)
        faqRecyclerView.adapter = faqadapter
        faqRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        loardfaqs()

        return  View
    }

    private fun loardfaqs() {
        val uid=auth.currentUser?.uid
        if (uid!=null){

            ref.addListenerForSingleValueEvent(object :ValueEventListener{
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