package com.astro.test.lafran.threading

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers


fun dispatchIO() = CoroutineScope(Dispatchers.IO)

fun dispatchMain() = CoroutineScope(Dispatchers.Main)