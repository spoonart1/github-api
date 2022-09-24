package com.astro.test.lafran.repository

import com.astro.test.lafran.database.UserDao
import com.astro.test.lafran.database.entity.UserEntity
import com.astro.test.lafran.network.ApiInterface
import com.astro.test.lafran.network.model.UserResponse
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import javax.inject.Inject

interface Repository {
    suspend fun fetchRemoteUsers(): List<UserResponse>
    suspend fun insertUsers(users: List<UserEntity>): Int
    suspend fun getUsers(): List<UserEntity>
}

@Module
@InstallIn(ActivityRetainedComponent::class)
class RepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val api: ApiInterface
) : Repository {

    override suspend fun fetchRemoteUsers() = api.getUsers()

    override suspend fun insertUsers(users: List<UserEntity>) = userDao.insert(users)

    override suspend fun getUsers() = userDao.getAll()
}