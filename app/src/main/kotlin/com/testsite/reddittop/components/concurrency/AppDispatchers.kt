package com.testsite.reddittop.components.concurrency

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

// NOTE: Don't use @Inject here since we won't be able to override it when needed
data class AppDispatchers(
    val Default: CoroutineDispatcher = Dispatchers.Default,
    val Main: CoroutineDispatcher = Dispatchers.Main,
    val IO: CoroutineDispatcher = Dispatchers.IO
)
