package com.example.helpinghand

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.location.LocationRequestCompat.Quality
import com.example.helpinghand.databinding.FragmentPostBinding

import com.google.common.primitives.Bytes

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.lang.ref.PhantomReference
import java.util.Base64


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
        firebaseDatabase = FirebaseDatabase.getInstance().getReference("users")

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
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null && imageUri != null && postDetail.isNotEmpty()) {
            val imageRef = storageReference.child("${System.currentTimeMillis()}.jpg")
            imageRef.putFile(imageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        val postId = firebaseDatabase.child("posts").push().key // Generate new push key
                        val post = hashMapOf(
                            "postDetail" to postDetail,
                            "imageUrl" to uri.toString()
                        )
                        firebaseDatabase.child("posts").child(postId!!) // Use the push key as child node's key
                            .setValue(post)
                            .addOnSuccessListener {
                                firebaseDatabase.child("users").child(userId).child("posts")
                                    .child(postId).setValue(true)
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            requireContext(),
                                            "Post uploaded successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(
                                            requireContext(),
                                            "Error uploading post: ${e.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(
                                    requireContext(),
                                    "Error uploading post: ${e.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
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
                "Please select an image and enter a post detail",
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
