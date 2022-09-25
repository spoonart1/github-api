package com.astro.test.lafran.network

import retrofit2.HttpException
import java.net.SocketTimeoutException

enum class NetworkState {
    Loading, Finished
}

object ErrorHandler {
    fun mapError(error: Throwable): String {
        return when (error) {
            is HttpException -> "Oops, something went wrong, try again later!"
            is SocketTimeoutException -> "Cannot reach the server, please check your connection"
            else -> error.message ?: "Unknown error"
        }
    }
}