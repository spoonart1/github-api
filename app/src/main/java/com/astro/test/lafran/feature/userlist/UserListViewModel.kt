package com.astro.test.lafran.feature.userlist

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.astro.test.lafran.database.OrderBy
import com.astro.test.lafran.database.entity.UserEntity
import com.astro.test.lafran.feature.userlist.adapter.UserListAdapter
import com.astro.test.lafran.network.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserListViewModel @Inject constructor(
    private val useCase: UserListUseCase
) : ViewModel() {

    private val sinceLiveData = MutableLiveData<Int>()
    private val filter = MutableLiveData<OrderBy>()
    private val networkStateLiveData = MutableLiveData<NetworkState>()

    val userList: LiveData<PagedList<UserEntity>> = filter.switchMap {
        useCase.getUsers(it)
    }

    val networkState: LiveData<NetworkState>
        get() = networkStateLiveData

    val since: LiveData<Int>
        get() = sinceLiveData

    init {
        fetchUser()
    }

    fun fetchUser() = viewModelScope.launch {
        val orderBy = filter.value ?: OrderBy.ASC
        val since = sinceLiveData.value ?: 0

        setNetworkState(NetworkState.Loading)
        useCase.fetchUser(orderBy, since).collect {
            setFilter(orderBy)
            setNetworkState(NetworkState.Finished)
        }
    }

    fun setFilter(orderBy: OrderBy) {
        this.filter.value = orderBy
    }

    fun setPage(since: Int) {
        sinceLiveData.value = since
    }

    private fun setNetworkState(state: NetworkState) {
        networkStateLiveData.value = state
    }

}