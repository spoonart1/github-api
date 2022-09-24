package com.astro.test.lafran.feature.userlist

import com.astro.test.lafran.database.entity.UserEntity
import com.astro.test.lafran.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface UserListUseCase {
    fun getUsers(): Flow<List<UserEntity>>
}

class UserListUseCaseImpl @Inject constructor(
    private val repository: Repository
) : UserListUseCase {

    override fun getUsers() = flow {
        val users = repository.fetchRemoteUsers()
        val entities = users.map {
            return@map UserEntity(
                userId = it.id,
                name = it.login,
                is_favorite = false
            )
        }
        repository.insertUsers(entities)
        val userData = repository.getUsers()
        emit(userData)
    }.flowOn(Dispatchers.IO)

}