package com.antoninovitale.dogs.common.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class DispatcherProvider @Inject constructor() {

    fun main(): CoroutineDispatcher =
        Dispatchers.Main

    fun io(): CoroutineDispatcher =
        Dispatchers.IO

    fun computation(): CoroutineDispatcher =
        Dispatchers.Default
}
