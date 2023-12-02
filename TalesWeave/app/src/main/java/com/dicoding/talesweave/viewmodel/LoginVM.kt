package com.dicoding.talesweave.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.talesweave.data.UsrModel
import com.dicoding.talesweave.repository.UsrRepository
import kotlinx.coroutines.launch

class LoginVM(private val repository: UsrRepository) : ViewModel() {
    fun saveSession(user: UsrModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun login(email: String, password: String) = repository.getLogin(email, password)
}