package com.astro.test.lafran.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

fun <T> LiveData<T>.getValueOrAwait(
    afterObserver: () -> Unit = {}
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(t: T) {
            data = t
            latch.countDown()
            this@getValueOrAwait.removeObserver(this)
        }
    }
    this.observeForever(observer)
    afterObserver()
    latch.await(5, TimeUnit.SECONDS)

    @Suppress("UNCHECKED_CAST")
    return data as T
}