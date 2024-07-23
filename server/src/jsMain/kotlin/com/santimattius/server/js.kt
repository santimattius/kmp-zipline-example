package com.santimattius.server

import app.cash.zipline.Zipline
import com.santimattius.kmp.core.SayHelloService

private val zipline by lazy { Zipline.get() }

@OptIn(ExperimentalJsExport::class)
@JsExport
fun main() {
    val service = BasicSayHello()
    zipline.bind<SayHelloService>(
        name = "SayHelloService",
        instance = service,
    )
}
