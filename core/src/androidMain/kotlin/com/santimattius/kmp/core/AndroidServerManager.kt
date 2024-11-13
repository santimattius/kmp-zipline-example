package com.santimattius.kmp.core

import android.content.Context
import app.cash.zipline.loader.ManifestVerifier.Companion.NO_SIGNATURE_CHECKS
import app.cash.zipline.loader.ZiplineCache
import app.cash.zipline.loader.ZiplineLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import okio.FileSystem
import okio.Path.Companion.toPath
import java.util.concurrent.Executors

class AndroidServerManager(
    private val context: Context,
    private val manifestUrl: String,
    override val scope: CoroutineScope
) : ServerManager {

    private val ziplineExecutorService = Executors.newSingleThreadExecutor { Thread(it, "Zipline") }
    private val ziplineDispatcher = ziplineExecutorService.asCoroutineDispatcher()
    private val okHttpClient = OkHttpClient()

    override val data = MutableStateFlow("")

    override fun start() {
        startZipline(
            scope = scope,
            dispatcher = ziplineDispatcher,
            loader = ZiplineLoader(
                dispatcher = ziplineDispatcher,
                manifestVerifier = NO_SIGNATURE_CHECKS,
                httpClient = okHttpClient,
            ).withCache(
                cache = ZiplineCache(
                    context = context,
                    fileSystem = FileSystem.SYSTEM,
                    directory = context.dataDir.path.toPath(),
                    maxSizeInBytes = (10 * 1024)
                ),
            ),
            manifestUrl = manifestUrl,
            data = data,
        )
    }

    override fun stop() {
        ziplineExecutorService.shutdown()
    }
}