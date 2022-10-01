package com.astro.test.lafran.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.astro.test.lafran.utils.TestCoroutineRule
import com.astro.test.lafran.network.ApiInterface
import com.astro.test.lafran.network.model.UserResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RepositoryTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var apiInterface: ApiInterface

    @InjectMocks
    private lateinit var repositoryImpl: RepositoryImpl

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_fetchRemoteUsers() {
        val userResponse = UserResponse(100, "abraham")
        val expected = listOf(userResponse)
        testCoroutineRule.runBlockingTest {
            `when`(apiInterface.getUsers(0)).thenReturn(expected)
            val result = repositoryImpl.fetchRemoteUsers(0)
            Assert.assertEquals(expected[0].id, result[0].id)
            Assert.assertEquals(expected[0].login, result[0].login)
            Assert.assertEquals(expected.size, result.size)
        }
    }
}