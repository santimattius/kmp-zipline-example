package com.santimattius.server

import com.santimattius.kmp.core.SayHelloService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class BasicSayHello : SayHelloService {
    override fun sayHello(): Flow<String> {
        return flowOf("Hello Medium")
    }
}