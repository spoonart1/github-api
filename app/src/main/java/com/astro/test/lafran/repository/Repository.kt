package com.astro.test.lafran.repository

import androidx.lifecycle.LiveData
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import androidx.sqlite.db.SimpleSQLiteQuery
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
    fun getUsers(orderBy: OrderBy, keyword: String?): LiveData<PagedList<UserEntity>>
}

@Module
@InstallIn(ActivityRetainedComponent::class)
class RepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val api: ApiInterface
) : Repository {

    companion object {
        val pagedListConfig = Config(30)
    }

    override suspend fun fetchRemoteUsers(since: Int) = api.getUsers(since)

    override suspend fun insertUsers(users: List<UserEntity>) = userDao.insert(users)

    override fun getUsers(orderBy: OrderBy, keyword: String?): LiveData<PagedList<UserEntity>> {
        val query =
            if (keyword.isNullOrEmpty()) SimpleSQLiteQuery("SELECT * from user ORDER BY user_id ${orderBy.name}")
            else SimpleSQLiteQuery("SELECT * from user where name LIKE '$keyword%' ORDER BY user_id ${orderBy.name}")
        return userDao.getAll(query).toLiveData(pagedListConfig)
    }
}