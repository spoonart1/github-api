package com.astro.test.lafran.feature.userlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.astro.test.lafran.database.OrderBy
import com.astro.test.lafran.database.entity.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val useCase: UserListUseCase
) : ViewModel() {
    private val sinceLiveData = MutableLiveData<Int>()

    val since: LiveData<Int>
        get() = sinceLiveData

    lateinit var userList: LiveData<PagingData<UserEntity>>

    init {
        //initial load
        setPage(0)
    }

    fun fetchUser(orderBy: OrderBy, since: Int) {
        viewModelScope.launch {
            useCase.getUsers(orderBy, since)
                .catch { it.printStackTrace() }
                .collect {
                    println(it)
                }
        }
    }

    private fun setPage(since: Int) {
        sinceLiveData.value = since
    }

}