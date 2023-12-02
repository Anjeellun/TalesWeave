package com.dicoding.talesweave.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.talesweave.repository.UsrRepository
import com.dicoding.talesweave.response.ListStoryItem

class MapsVM(private val usrRepository: UsrRepository): ViewModel() {
    fun getStoryLoc() = usrRepository.getStory()

}