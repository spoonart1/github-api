package com.astro.test.lafran.repository

import com.astro.test.lafran.network.ApiInterface
import com.astro.test.lafran.network.Client
import com.astro.test.lafran.network.model.UserResponseModel

interface Repository {
    suspend fun getUser(): List<UserResponseModel>
}

class RepositoryImpl : Repository {

    private val serviceApi: ApiInterface = Client.API

    override suspend fun getUser(): List<UserResponseModel> {
        return serviceApi.getUsers()
    }
}