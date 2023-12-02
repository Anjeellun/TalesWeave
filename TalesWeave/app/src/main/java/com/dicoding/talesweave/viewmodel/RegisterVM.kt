package com.dicoding.talesweave.viewmodel

import androidx.lifecycle.ViewModel
import com.dicoding.talesweave.repository.UsrRepository

class RegisterVM(private val repository: UsrRepository) : ViewModel() {
    fun signup(name: String, email: String, password: String) =
        repository.getRegister(name, email, password)


}