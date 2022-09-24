package com.astro.test.lafran.network

import com.astro.test.lafran.network.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("/users")
    suspend fun getUsers(@Query("since") since: Int): List<UserResponse>

}