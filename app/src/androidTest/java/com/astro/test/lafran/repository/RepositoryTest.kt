package com.astro.test.lafran.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.DataSource
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.astro.test.lafran.database.AppDatabase
import com.astro.test.lafran.database.OrderBy
import com.astro.test.lafran.database.UserDao
import com.astro.test.lafran.database.entity.UserEntity
import com.astro.test.lafran.network.ApiInterface
import com.astro.test.lafran.network.model.UserResponse
import com.astro.test.lafran.utils.MockingProvider
import com.astro.test.lafran.utils.TestCoroutineRule
import com.astro.test.lafran.utils.getValueOrAwait
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RepositoryTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var userDao: UserDao

    private val apiInterface = object : ApiInterface {
        override suspend fun getUsers(since: Int): List<UserResponse> {
            return listOf()
        }

        override fun getUsers(orderBy: OrderBy): DataSource.Factory<Int, UserEntity> {
            return dataSource
        }

    }

    private lateinit var repositoryImpl: RepositoryImpl

    private lateinit var db: AppDatabase

    @Before
    fun init() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java
        ).build()

        userDao = db.userDao()
        repositoryImpl = RepositoryImpl(userDao, apiInterface)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_getUsers() {
        val itemList = listOf(
            UserEntity(100, "abraham", false),
            UserEntity(101, "abraham1", false),
            UserEntity(102, "abraham2", false),
            UserEntity(103, "abraham3", false)
        )
        db.runInTransaction {
            testCoroutineRule.runBlockingTest {
                userDao.insert(itemList)
                val result = repositoryImpl.getUsers(OrderBy.ASC, null)
                val liveData = MockingProvider.liveDataPaged(result)
                val value = liveData.getValueOrAwait()
                for (i in itemList.indices) {
                    assertEquals(
                        "expected userId value is not equal with given value",
                        itemList[i].userId,
                        value[i]!!.userId
                    )
                    assertEquals(
                        "expected name value is not equal with given value",
                        itemList[i].name,
                        value[i]!!.name
                    )
                    assertEquals(
                        "expected isFavorite value is not equal with given value",
                        itemList[i].is_favorite,
                        value[i]!!.is_favorite
                    )
                }

            }
        }
    }

    private val dataSource by lazy {
        val itemList = listOf(UserEntity(100, "abraham", false))
        return@lazy MockingProvider.dataSourceFactory(itemList)
    }

}