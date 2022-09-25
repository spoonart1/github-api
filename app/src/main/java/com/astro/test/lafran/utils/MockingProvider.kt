package com.astro.test.lafran.utils

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.astro.test.lafran.database.entity.UserEntity

class MockingProvider {

    fun emptyPagedList(): LiveData<PagedList<UserEntity>> {
        return dataSourceFactory().toLiveData(Config(30))
    }

    private fun dataSourceFactory(): DataSource.Factory<Int, UserEntity> {
        return object : DataSource.Factory<Int, UserEntity>() {
            override fun create(): DataSource<Int, UserEntity> {
                return datasource()
            }
        }
    }

    private fun datasource(): PageKeyedDataSource<Int, UserEntity> {
        return object : PageKeyedDataSource<Int, UserEntity>() {
            override fun loadInitial(
                params: LoadInitialParams<Int>,
                callback: LoadInitialCallback<Int, UserEntity>
            ) {
            }

            override fun loadBefore(
                params: LoadParams<Int>,
                callback: LoadCallback<Int, UserEntity>
            ) {
            }

            override fun loadAfter(
                params: LoadParams<Int>,
                callback: LoadCallback<Int, UserEntity>
            ) {
            }

        }
    }

}