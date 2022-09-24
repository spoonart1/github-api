package com.astro.test.lafran

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.astro.test.lafran.feature.userlist.UserListUseCase
import com.astro.test.lafran.feature.userlist.UserListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UserListViewModelTest {
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var viewModel: UserListViewModel

    private lateinit var userListUseCase: UserListUseCase

    @Before
    fun setup() {
        userListUseCase = Mockito.mock(UserListUseCase::class.java)
        viewModel = UserListViewModel(userListUseCase)
    }

    @Test
    fun test_when_initializing_viewModel_Should_return_SinceValue_zero() {
        assertEquals(0, viewModel.since.value)
    }

    @Test
    fun test_when_setPage_SinceLiveDataValue_shouldReturnExactValue() {
        viewModel.setPage(1)
        assertEquals(1, viewModel.since.value)
    }

}