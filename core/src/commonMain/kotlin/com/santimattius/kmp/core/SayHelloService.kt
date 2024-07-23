package com.santimattius.kmp.core

import app.cash.zipline.ZiplineService
import kotlinx.coroutines.flow.Flow

interface SayHelloService : ZiplineService {
    fun sayHello(): Flow<String>
}