package com.santimattius.kmp.core

import app.cash.zipline.EngineApi
import app.cash.zipline.QuickJs
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(EngineApi::class)
class AndroidQuickJsTest {

    @Test
    fun runJavaScript() {
        val quickJs = QuickJs.create()
        assertEquals(
            expected = "Hello World",
            actual = quickJs.evaluate("['Hello', 'World'].join(' ')")
        )
        quickJs.close()
    }
}