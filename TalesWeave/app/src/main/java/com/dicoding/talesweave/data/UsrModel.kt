package com.dicoding.talesweave.data

data class UsrModel(
    val name: String,
    val userId: String,
    val token: String,
    val isLogin: Boolean = false
)
