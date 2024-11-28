package com.santimattius.kmp.skeleton.di

import android.content.Context
import com.santimattius.kmp.core.AndroidServerManager
import com.santimattius.kmp.core.ServerManager
import com.santimattius.kmp.skeleton.core.file.AssetFileSystem
import kmp_zipline_example.composeapp.generated.resources.Res
import com.santimattius.kmp.skeleton.applicationContext
import kotlinx.coroutines.MainScope
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

@OptIn(ExperimentalResourceApi::class)
actual val platformModule: Module
    get() = module {
        factory<Context> {
            val context = applicationContext ?: run {
                throw IllegalArgumentException("Application context is null")
            }
            context
        }
        factory<ServerManager> {
            val path = Res.getUri("files/zipline-example.manifest.zipline.json").substringBeforeLast("/")
            AndroidServerManager(
                context = get(),
                manifestUrl = "http://10.0.2.2:8080/manifest.zipline.json",
                scope = MainScope(),
                path = AssetFileSystem(androidContext()).readFile(path).toString()
            )
        }
    }