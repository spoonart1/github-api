package com.astro.test.lafran.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: Int,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "is_favorite")
    val is_favorite: Boolean
)
