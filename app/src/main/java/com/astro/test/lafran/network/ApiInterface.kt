package com.astro.test.lafran.network

import com.astro.test.lafran.network.model.UserResponseModel
import retrofit2.http.GET

interface ApiInterface {

    @GET("/users")
    suspend fun getUsers(): List<UserResponseModel>

}