package com.santimattius.kmp.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

interface ServerManager {

    val data: MutableStateFlow<String>

    val scope: CoroutineScope

    fun start()

    fun stop()
}