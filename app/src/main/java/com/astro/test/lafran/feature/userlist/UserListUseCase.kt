package com.astro.test.lafran.feature.userlist

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.astro.test.lafran.database.OrderBy
import com.astro.test.lafran.database.entity.UserEntity
import com.astro.test.lafran.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface UserListUseCase {
    fun fetchUser(orderBy: OrderBy, since: Int): Flow<Int>

    fun getUsers(orderBy: OrderBy, keyword: String?): LiveData<PagedList<UserEntity>>
}

class UserListUseCaseImpl @Inject constructor(
    private val repository: Repository
) : UserListUseCase {

    override fun fetchUser(orderBy: OrderBy, since: Int) = flow {
        val users = repository.fetchRemoteUsers(since)
        val entities = users.map {
            return@map UserEntity(
                userId = it.id,
                name = it.login,
                is_favorite = false
            )
        }
        repository.insertUsers(entities)
        kotlinx.coroutines.delay(2000)
        emit(entities.size)
    }.flowOn(Dispatchers.IO)

    override fun getUsers(orderBy: OrderBy, keyword: String?) = repository.getUsers(orderBy, keyword)
}