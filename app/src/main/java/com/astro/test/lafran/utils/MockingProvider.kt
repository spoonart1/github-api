package com.astro.test.lafran.utils

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.astro.test.lafran.database.entity.UserEntity

object MockingProvider {

    fun dataSourceFactory(list: List<UserEntity>): DataSource.Factory<Int, UserEntity> {
        return object : DataSource.Factory<Int, UserEntity>() {
            override fun create(): DataSource<Int, UserEntity> {
                return datasource(list)
            }
        }
    }

    fun config() = PagedList.Config.Builder()
        .setInitialLoadSizeHint(30)
        .setPageSize(30)
        .build()

    fun liveDataPaged(dataSource: DataSource.Factory<Int, UserEntity>): LiveData<PagedList<UserEntity>> {
        return LivePagedListBuilder(dataSource, config()).build()
    }

    fun datasource(list: List<UserEntity>): PageKeyedDataSource<Int, UserEntity> {
        return object : PageKeyedDataSource<Int, UserEntity>() {
            override fun loadInitial(
                params: LoadInitialParams<Int>,
                callback: LoadInitialCallback<Int, UserEntity>
            ) {
                val result: List<UserEntity> = list
                callback.onResult(result, 1, 2)
            }

            override fun loadBefore(
                params: LoadParams<Int>,
                callback: LoadCallback<Int, UserEntity>
            ) {
                callback.onResult(list, 0)
            }

            override fun loadAfter(
                params: LoadParams<Int>,
                callback: LoadCallback<Int, UserEntity>
            ) {
                callback.onResult(list, 0)
            }

        }
    }
}