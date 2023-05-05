package com.example.helpinghand.Models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.helpinghand.Repository.CommentRepository
import kotlinx.coroutines.launch

class CommentViewModel() : ViewModel() {

    private val repository : CommentRepository = CommentRepository().getInstance()
    private  val _allComments = MutableLiveData<List<Comment>>()
    val allComments : LiveData<List<Comment>> = _allComments

    init {
        this.repository.loadComments(_allComments)
    }

}