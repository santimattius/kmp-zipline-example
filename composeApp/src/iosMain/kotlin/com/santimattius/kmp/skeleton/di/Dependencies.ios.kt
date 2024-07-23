package com.santimattius.kmp.skeleton.di

import com.santimattius.kmp.core.IOSServerManager
import com.santimattius.kmp.core.ServerManager
import kotlinx.coroutines.MainScope
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        factory<ServerManager> {
            IOSServerManager(
                manifestUrl = "http://localhost:8080/manifest.zipline.json",
                scope = MainScope()
            )
        }
    }