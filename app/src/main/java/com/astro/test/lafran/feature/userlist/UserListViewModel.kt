package com.astro.test.lafran.feature.userlist

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.astro.test.lafran.database.OrderBy
import com.astro.test.lafran.database.entity.UserEntity
import com.astro.test.lafran.network.NetworkState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserListViewModel @Inject constructor(
    private val useCase: UserListUseCase
) : ViewModel() {

    val filter = MutableLiveData<Pair<OrderBy, String?>>()
    private val sinceLiveData = MutableLiveData<Int>()
    private val errorLiveData = MutableLiveData<Throwable>()
    private val networkStateLiveData = MutableLiveData<NetworkState>()

    val userList: LiveData<PagedList<UserEntity>> = filter.switchMap {
        useCase.getUsers(it.first, it.second)
    }

    val networkState: LiveData<NetworkState>
        get() = networkStateLiveData

    val since: LiveData<Int>
        get() = sinceLiveData

    val error: LiveData<Throwable>
        get() = errorLiveData

    init {
        fetchUser()
    }

    fun fetchUser() = viewModelScope.launch {
        val orderBy = filter.value?.first ?: OrderBy.ASC
        val since = sinceLiveData.value ?: 0

        setNetworkState(NetworkState.Loading)
        useCase.fetchUser(orderBy, since)
            .catch { throwable ->
                errorLiveData.postValue(throwable)
                setNetworkState(NetworkState.Finished)
            }
            .collect {
                setFilter(orderBy)
                setNetworkState(NetworkState.Finished)
            }
    }

    fun setFilter(orderBy: OrderBy) {
        this.filter.value = Pair(orderBy, null)
    }

    fun setKeyword(keyword: String) {
        val currentFilter = filter.value?.first ?: OrderBy.ASC
        this.filter.value = Pair(currentFilter, keyword)
    }

    fun setPage(since: Int) {
        sinceLiveData.value = since
    }

    private fun setNetworkState(state: NetworkState) {
        networkStateLiveData.value = state
    }

}