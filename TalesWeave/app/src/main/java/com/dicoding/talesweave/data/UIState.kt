package com.dicoding.talesweave.data

sealed class UIState<out R> private constructor(){
    data class Success<out T>(val data: T) : UIState<T>()
    data class Error(val error: String) : UIState<Nothing>()
    object Loading : UIState<Nothing>()
}
