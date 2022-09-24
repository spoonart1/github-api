package com.astro.test.lafran.repository

import com.astro.test.lafran.database.OrderBy
import com.astro.test.lafran.database.UserDao
import com.astro.test.lafran.database.entity.UserEntity
import com.astro.test.lafran.network.ApiInterface
import com.astro.test.lafran.network.model.UserResponse
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import javax.inject.Inject

interface Repository {
    suspend fun fetchRemoteUsers(since: Int = 1): List<UserResponse>
    suspend fun insertUsers(users: List<UserEntity>)
    suspend fun getUsers(orderBy: OrderBy): List<UserEntity>
}

@Module
@InstallIn(ActivityRetainedComponent::class)
class RepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val api: ApiInterface
) : Repository {

    override suspend fun fetchRemoteUsers(since: Int) = api.getUsers(since)

    override suspend fun insertUsers(users: List<UserEntity>) = userDao.insert(users)

    override suspend fun getUsers(orderBy: OrderBy) = userDao.getAll(orderBy.name)
}