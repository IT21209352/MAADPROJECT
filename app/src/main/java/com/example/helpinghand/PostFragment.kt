package com.example.helpinghand

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.helpinghand.databinding.FragmentPostBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class PostFragment : Fragment() {

    private lateinit var binding: FragmentPostBinding
    private lateinit var storageReference: StorageReference
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseDatabase: DatabaseReference
    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPostBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        storageReference = FirebaseStorage.getInstance().getReference("images")
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("posts")

        binding.imageView.setOnClickListener {
            openGallery()
        }

        binding.postButton.setOnClickListener {
            uploadPost()
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
    }

    private fun uploadPost() {
        val postDetail = binding.postDetailTextView.text.toString()
        val medPrice =Integer.parseInt( binding.postPrice.text.toString())
        val userId = firebaseAuth.currentUser?.email
        val medName = binding.medName.text.toString()

        if (userId != null && imageUri != null && postDetail.isNotEmpty() && medPrice > 0) {
            val imageRef = storageReference.child("${System.currentTimeMillis()}.jpg")
            imageRef.putFile(imageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        val postId = firebaseDatabase.push().key // Generate new push key

                        val post = hashMapOf(
                            "postDetail" to postDetail,
                            "imageUrl" to uri.toString(),
                            "post_owner" to userId,
                            "post_key" to postId,
                            "post_ownerID" to  firebaseAuth.currentUser?.uid,
                            "medName" to medName,
                            "medPrice" to medPrice
                        )
                        firebaseDatabase.child(postId!!) // Use the push key as child node's key
                            .setValue(post)
                            .addOnSuccessListener {

                                Toast.makeText(
                                    requireContext(),
                                    "Medicine uploaded successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val myPostsFragment = myPostsFragment()
                                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                                transaction.replace(R.id.fragment_container_1, myPostsFragment)
                                transaction.addToBackStack(null)
                                transaction.commit()
                            }

                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        requireContext(),
                        "Error uploading image: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            Toast.makeText(
                requireContext(),
                "Please select an image and enter all the details of the medicine",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            binding.imageView.setImageURI(imageUri)
        }
    }

    companion object {
        private const val REQUEST_IMAGE_GALLERY = 100
    }



}