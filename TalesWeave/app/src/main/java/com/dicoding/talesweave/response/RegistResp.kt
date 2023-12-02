package com.dicoding.talesweave.response

import com.google.gson.annotations.SerializedName

data class RegistResp(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
