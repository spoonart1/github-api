package com.astro.test.lafran.entity

import com.astro.test.lafran.database.entity.UserEntity
import org.junit.Assert
import org.junit.Test


class UserEntityTest {

    @Test
    fun testUserEntity() {
        val userEntity = UserEntity(
            userId = 10,
            name = "astronouts",
            is_favorite = true
        )

        Assert.assertTrue(userEntity.userId == 10)
        Assert.assertTrue(userEntity.name == "astronouts")
        Assert.assertTrue(userEntity.is_favorite)
    }

}