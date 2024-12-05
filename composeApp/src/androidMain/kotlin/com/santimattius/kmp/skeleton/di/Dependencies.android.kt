package com.santimattius.kmp.skeleton.di

import android.content.Context
import com.santimattius.kmp.core.AndroidServerManager
import com.santimattius.kmp.core.ServerManager
import com.santimattius.kmp.skeleton.applicationContext
import kmp_zipline_example.composeapp.generated.resources.Res
import kotlinx.coroutines.MainScope
import org.jetbrains.compose.resources.ExperimentalResourceApi
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
            val path = getManifestPath()
            AndroidServerManager(
                context = get(),
                manifestUrl = "http://10.0.2.2:8080/manifest.zipline.json",
                scope = MainScope(),
                path = path
            )
        }
    }

@OptIn(ExperimentalResourceApi::class)
private fun getManifestPath() =
    Res.getUri("files/zipline-example.manifest.zipline.json")
        .substringAfterLast("file:///android_asset/").substringBeforeLast("/")