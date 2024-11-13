package com.santimattius.kmp.skeleton.di

import android.content.Context
import com.santimattius.kmp.core.AndroidServerManager
import com.santimattius.kmp.core.ServerManager
import com.santimattius.kmp.skeleton.applicationContext
import kotlinx.coroutines.MainScope
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        factory<Context> {
            val context = applicationContext ?: run {
                throw IllegalArgumentException("Application context is null")
            }
            context
        }
        factory<ServerManager> {
            AndroidServerManager(
                context = get(),
                manifestUrl = "http://10.0.2.2:8080/manifest.zipline.json",
                scope = MainScope()
            )
        }
    }