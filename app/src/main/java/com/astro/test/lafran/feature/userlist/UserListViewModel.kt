package com.astro.test.lafran.feature.userlist

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.astro.test.lafran.database.OrderBy
import com.astro.test.lafran.database.entity.UserEntity
import com.astro.test.lafran.feature.userlist.adapter.UserListAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserListViewModel @Inject constructor(
    private val useCase: UserListUseCase
) : ViewModel() {

    private val sinceLiveData = MutableLiveData<Int>()
    private val filter = MutableLiveData<OrderBy>()
    private val networkStateLiveData = MutableLiveData<UserListAdapter.NetworkState>()

    val userList: LiveData<PagedList<UserEntity>> = filter.switchMap {
        useCase.getUsers(it)
    }

    val networkState: LiveData<UserListAdapter.NetworkState>
        get() = networkStateLiveData

    init {
        fetchUser(OrderBy.ASC, 0)
    }

    fun fetchUser(orderBy: OrderBy, since: Int) = viewModelScope.launch {
        setNetworkState(UserListAdapter.NetworkState.Loading)
        useCase.fetchUser(orderBy, since).collect {
            setFilter(orderBy)
            setNetworkState(UserListAdapter.NetworkState.Finished)
        }
    }

    fun setFilter(orderBy: OrderBy) {
        this.filter.value = orderBy
    }

    fun setPage(since: Int) {
        sinceLiveData.value = since
    }

    private fun setNetworkState(state: UserListAdapter.NetworkState) {
        networkStateLiveData.value = state
    }

}