package com.dicoding.talesweave.response

import com.google.gson.annotations.SerializedName

data class AddResp(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
