package com.example.helpinghand.Repository

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.helpinghand.Models.Comment
import com.google.firebase.database.*

class CommentRepository {
    //private val databaseReference : DatabaseReference = FirebaseDatabase.getInstance().getReference("post_comments")
    private val databaseReference : DatabaseReference =  FirebaseDatabase.getInstance("https://maad-bb9db-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("comments/post_comments")

    @Volatile private  var INSTANCE : CommentRepository ?= null

    fun getInstance(): CommentRepository{
        return INSTANCE ?: synchronized(this){
            val instance = CommentRepository()
            INSTANCE = instance
            instance
        }
    }


    fun loadComments(commentList : MutableLiveData<List<Comment>>){
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val _commentList : List<Comment> = snapshot.children.map { dataSnapshot ->
                        dataSnapshot.getValue(Comment::class.java)!!
                    }

                    commentList.postValue(_commentList)

                }catch (e:Exception){
                    Log.d(ContentValues.TAG, "$e")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    fun addComment(comment: Comment) {
        val commentId = databaseReference.push().key //generate unique id for comment
        commentId?.let {
            databaseReference.child(it).setValue(comment)
        }
    }

    fun deleteComment(commentId: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val commentRef = databaseReference.child(commentId)

        commentRef.removeValue { error, _ ->
            if (error == null) {
                onSuccess.invoke()
            } else {
                onError.invoke(error.message)
            }
        }
    }
}