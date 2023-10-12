package com.example.helpinghand.adapters

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.helpinghand.Models.FAQ
import com.example.helpinghand.R
import com.google.android.play.core.integrity.e
import com.google.firebase.database.FirebaseDatabase

class faqadapter(private val faqs:MutableList<FAQ>): RecyclerView.Adapter<faqadapter.ViewHolder>() {

    private val colors = arrayOf("#E1BEE7","#D1C4E9", "#C5CAE9", "#BBDEFB", "#B3E5FC", "#B2EBF2", "#B2DFDB", "#C8E6C9")
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        val faqText: TextView = itemView.findViewById(R.id.tvfaq)
        val btndelete: Button = itemView.findViewById(R.id.delete)
        val bntupdate: Button = itemView.findViewById(R.id.update)
        val faql:RelativeLayout = itemView.findViewById(R.id.faqLayout)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.faq,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val faq=faqs[position]
        holder.faqText.text=faq.faq
        val color = colors[position % colors.size]
        holder.faql.setBackgroundColor(Color.parseColor(color))
        holder.bntupdate.setOnClickListener {

            val editFaqDialog = Dialog(holder.itemView.context)
            editFaqDialog.setContentView(R.layout.editfaq)

            // Set the existing FAQ text as the hint in the EditText
            editFaqDialog.findViewById<EditText>(R.id.editFaqText).hint = faq.faq

            editFaqDialog.findViewById<Button>(R.id.updateFaqButton).setOnClickListener {
                val newFaqText = editFaqDialog.findViewById<EditText>(R.id.editFaqText).text.toString()

                // Update the FAQ text in the database
                faq.faqid?.let { id ->
                    FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app")
                        .getReference("faq").child(id)
                        .child("faq")
                        .setValue(newFaqText)
                        .addOnSuccessListener {
                            // Update the FAQ text in the local list
                            faq.faq = newFaqText
                            notifyDataSetChanged()
                            Toast.makeText(
                                holder.itemView.context,
                                "FAQ updated successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                            editFaqDialog.dismiss()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                holder.itemView.context,
                                "Failed to update FAQ: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }

            editFaqDialog.show()

        }
        holder.btndelete.setOnClickListener {

            faq.faqid?.let { id->
                FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("faq").child(id)
                    .removeValue()
                    .addOnSuccessListener {
                        //remvoe faq from faq list
                        faqs.remove(faq)
                        notifyDataSetChanged()

                        Toast.makeText(holder.itemView.context,"successful",Toast.LENGTH_SHORT).show()

                    }
                    .addOnFailureListener { e ->

                        Toast.makeText(holder.itemView.context,"Failed to delete: ${e.message}",Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    override fun getItemCount(): Int {
        return faqs.size
    }




}