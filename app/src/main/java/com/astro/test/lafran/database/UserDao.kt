package com.astro.test.lafran.database

import androidx.room.*
import com.astro.test.lafran.database.entity.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userEntity: List<UserEntity>): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(userEntity: UserEntity): Int

    @Query("SELECT * from user")
    suspend fun getAll(): List<UserEntity>

}