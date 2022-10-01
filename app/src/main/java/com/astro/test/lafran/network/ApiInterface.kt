package com.astro.test.lafran.network

import androidx.paging.DataSource
import com.astro.test.lafran.database.OrderBy
import com.astro.test.lafran.database.entity.UserEntity
import com.astro.test.lafran.network.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("/users")
    suspend fun getUsers(@Query("since") since: Int): List<UserResponse>

    fun getUsers(orderBy: OrderBy): DataSource.Factory<Int, UserEntity>

}