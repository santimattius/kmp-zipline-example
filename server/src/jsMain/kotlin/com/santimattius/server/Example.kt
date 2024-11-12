package com.santimattius.server

@OptIn(ExperimentalJsExport::class)
@JsExport
fun sayHello(): String {
    return listOf("Hello", "World")
        .joinToString(" ")
}