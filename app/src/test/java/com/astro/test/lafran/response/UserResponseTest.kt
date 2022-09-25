package com.astro.test.lafran.response

import com.astro.test.lafran.network.model.UserResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class UserResponseTest {

    @Test
    fun testUserResponse() {
        val response = UserResponse(100, "abraham")
        assertEquals(100, response.id)
        assertEquals("abraham", response.login)
    }
}