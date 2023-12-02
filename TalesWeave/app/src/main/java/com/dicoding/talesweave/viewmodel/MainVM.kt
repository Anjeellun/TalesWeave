package com.dicoding.talesweave.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.talesweave.data.UsrModel
import com.dicoding.talesweave.repository.UsrRepository
import com.dicoding.talesweave.response.ListStoryItem
import kotlinx.coroutines.launch

class MainVM(private val usrRepository: UsrRepository) : ViewModel() {
    fun getSession(): LiveData<UsrModel> {
        return usrRepository.getSession().asLiveData()

    }

    fun logout() {
        viewModelScope.launch {
            usrRepository.logout()
        }
    }

    val storyPager: LiveData<PagingData<ListStoryItem>> =
        usrRepository.getStoriesPager().cachedIn(viewModelScope)
}
