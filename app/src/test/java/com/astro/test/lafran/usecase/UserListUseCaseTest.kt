package com.astro.test.lafran.usecase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.astro.test.lafran.utils.TestCoroutineRule
import com.astro.test.lafran.database.OrderBy
import com.astro.test.lafran.database.entity.UserEntity
import com.astro.test.lafran.feature.userlist.UserListUseCaseImpl
import com.astro.test.lafran.network.model.UserResponse
import com.astro.test.lafran.repository.Repository
import com.astro.test.lafran.utils.createMockDataSourceFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserListUseCaseTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var repository: Repository

    @InjectMocks
    private lateinit var useCaseImpl: UserListUseCaseImpl

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_fetchUsers() {
        testCoroutineRule.runBlockingTest {
            val response = listResponse()
            val entities = response.map { UserEntity(it.id, it.login, false) }
            `when`(repository.fetchRemoteUsers(0)).thenReturn(response)
            `when`(repository.insertUsers(entities)).thenReturn(listOf(entities.size.toLong()))
            val result = useCaseImpl.fetchUser(OrderBy.ASC, 0)
            result.collect {
                assertEquals("Expecting value ${entities.size} but found $it", entities.size, it)
            }
        }
    }

    @Test
    fun getTest() {
        val list = listEntities()
        val datasourceFactory = createMockDataSourceFactory(list)
        `when`(repository.getUsers(OrderBy.ASC, null)).thenReturn(datasourceFactory)
        val result = useCaseImpl.getUsers(OrderBy.ASC, null)

        assertNotNull(result)
        assertEquals(datasourceFactory.hashCode(), result.hashCode())
    }

    private fun listResponse() = listOf(
        UserResponse(100, "abc"),
        UserResponse(101, "abcd"),
        UserResponse(102, "abcde"),
        UserResponse(103, "abcdef"),
        UserResponse(104, "abcdefg")
    )

    private fun listEntities() = listResponse().map {
        UserEntity(it.id, it.login, false)
    }

}