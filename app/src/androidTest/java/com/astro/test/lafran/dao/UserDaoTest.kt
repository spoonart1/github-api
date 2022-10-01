package com.astro.test.lafran.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.Config
import androidx.paging.PagedList
import androidx.paging.toLiveData
import androidx.room.Room
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.astro.test.lafran.database.AppDatabase
import com.astro.test.lafran.database.entity.UserEntity
import com.astro.test.lafran.utils.TestCoroutineRule
import com.astro.test.lafran.utils.getValueOrAwait
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.hamcrest.CoreMatchers.`is` as Is

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var db: AppDatabase

    @Before
    fun init() {
        db = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            AppDatabase::class.java
        ).build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_getUser_shouldReturn_1() {

        val user = listOf(UserEntity(100, "test", false))

        db.runInTransaction {
            testCoroutineRule.runBlockingTest {
                db.userDao().insert(user)
            }
        }

        val query = SimpleSQLiteQuery("SELECT * from user")
        val livedata = db.userDao().getAll(query).toLiveData(Config(30))

        val items = livedata.getValueOrAwait()
        assertThat(items.size, Is(1))
    }

    @Test
    fun test_getUser_shouldReturn_empty() {
        val query = SimpleSQLiteQuery("SELECT * from user")
        val livedata = db.userDao().getAll(query).toLiveData(Config(30))
        val items = livedata.getValueOrAwait()
        assertThat(items.size, Is(0))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun test_updateUser_shouldReturn_asGivenValue() {
        val users = listOf(UserEntity(100, "test", false))
        db.runInTransaction {
            testCoroutineRule.runBlockingTest {
                db.userDao().insert(users)

                getValue("SELECT * from user").also { items ->
                    assertThat(items[0]!!.name, Is("test"))
                }

                val user = UserEntity(100, "test-update", false)
                db.userDao().update(user)

                getValue("SELECT * from user where user_id = ${user.userId}").also { items ->
                    assertThat(items[0]!!.name, Is("test-update"))
                }
            }
        }
    }

    private fun getValue(query: String): PagedList<UserEntity> {
        val livedata = db.userDao().getAll(SimpleSQLiteQuery(query)).toLiveData(Config(30))
        return livedata.getValueOrAwait()
    }

}