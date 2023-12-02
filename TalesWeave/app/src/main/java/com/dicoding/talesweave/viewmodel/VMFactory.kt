package com.dicoding.talesweave.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.talesweave.data.Injection
import com.dicoding.talesweave.repository.UsrRepository

class VMFactory(private val repository: UsrRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainVM::class.java) -> {
                MainVM(repository) as T
            }

            modelClass.isAssignableFrom(LoginVM::class.java) -> {
                LoginVM(repository) as T
            }

            modelClass.isAssignableFrom(RegisterVM::class.java) -> {
                RegisterVM(repository) as T
            }

            modelClass.isAssignableFrom(StoryVM::class.java) -> {
                StoryVM(repository) as T
            }

            modelClass.isAssignableFrom(MapsVM::class.java) -> {
                MapsVM(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: VMFactory? = null

        @JvmStatic
        fun getInstance(context: Context): VMFactory {
            if (INSTANCE == null) {
                synchronized(VMFactory::class.java) {
                    INSTANCE = VMFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as VMFactory
        }
    }

}