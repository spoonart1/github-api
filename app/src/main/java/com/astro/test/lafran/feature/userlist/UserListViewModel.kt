package com.astro.test.lafran.feature.userlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val useCase: UserListUseCase
) : ViewModel() {

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            useCase.getUsers()
                .catch { it.printStackTrace() }
                .collect { data ->
                    print(data)
                }
        }
    }

}