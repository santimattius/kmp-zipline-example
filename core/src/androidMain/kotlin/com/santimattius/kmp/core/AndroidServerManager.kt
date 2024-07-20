package com.santimattius.kmp.core

import app.cash.zipline.loader.ManifestVerifier.Companion.NO_SIGNATURE_CHECKS
import app.cash.zipline.loader.ZiplineLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import java.util.concurrent.Executors

//todo:"http://10.0.2.2:8080/manifest.zipline.json"
class AndroidServerManager(
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
            ),
            manifestUrl = manifestUrl,
            data = data,
        )
    }

    override fun stop() {
        ziplineExecutorService.shutdown()
    }
}