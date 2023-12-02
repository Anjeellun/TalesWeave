package com.dicoding.talesweave.viewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.talesweave.repository.UsrRepository
import java.io.File

class StoryVM(private val usrRepository: UsrRepository) : ViewModel() {
    fun postImage(photo: File, description: String) = usrRepository.postStory(photo, description)

}