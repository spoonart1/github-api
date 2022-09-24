package com.astro.test.lafran.network.model

import com.google.gson.annotations.SerializedName

data class UserResponseModel(
    @SerializedName("id")
    val id: Int,

    @SerializedName("login")
    val login: String
)