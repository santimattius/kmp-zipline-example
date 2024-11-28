package com.santimattius.kmp.skeleton.di

import com.santimattius.kmp.core.AndroidServerManager
import com.santimattius.kmp.core.ServerManager
import com.santimattius.kmp.skeleton.core.file.AssetFileSystem
import kmp_zipline_example.composeapp.generated.resources.Res
import kotlinx.coroutines.MainScope
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

@OptIn(ExperimentalResourceApi::class)
actual val platformModule: Module
    get() = module {
        factory<ServerManager> {
            val path = Res.getUri("files/zipline-example.manifest.zipline.json").substringBeforeLast("/")
            AndroidServerManager(
                manifestUrl = "http://10.0.2.2:8080/manifest.zipline.json",
                scope = MainScope(),
                path = AssetFileSystem(androidContext()).readFile(path).toString()
            )
        }
    }