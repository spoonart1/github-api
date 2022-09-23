package com.astro.test.lafran

import com.astro.test.lafran.database.entity.UserEntity
import org.junit.Assert
import org.junit.Test


class UserTest {

    @Test
    fun testUserEntity() {
        val userEntity = UserEntity(
            userId = 10,
            name = "astronouts",
            imageUrl = "www.image.url/photo/astronouts",
            is_favorite = true
        )

        Assert.assertTrue(userEntity.userId == 10)
        Assert.assertTrue(userEntity.name == "astronouts")
        Assert.assertTrue(userEntity.imageUrl == "www.image.url/photo/astronouts")
        Assert.assertTrue(userEntity.is_favorite)
    }

}