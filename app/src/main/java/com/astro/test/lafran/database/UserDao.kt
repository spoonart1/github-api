package com.astro.test.lafran.database

import androidx.paging.DataSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.astro.test.lafran.database.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userEntity: List<UserEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(userEntity: UserEntity): Int

    @RawQuery(observedEntities = [UserEntity::class])
    fun getAll(query: SupportSQLiteQuery): DataSource.Factory<Int, UserEntity>

}