package com.santimattius.kmp.core

import android.content.Context
import app.cash.zipline.loader.ManifestVerifier.Companion.NO_SIGNATURE_CHECKS
import app.cash.zipline.loader.ZiplineLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import okio.Path.Companion.toPath
import okio.assetfilesystem.asFileSystem
import java.util.concurrent.Executors

class AndroidServerManager(
    private val context: Context,
    private val manifestUrl: String,
    override val scope: CoroutineScope,
    override val path: String
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
            ).withEmbedded(
                embeddedFileSystem = context.assets.asFileSystem(),
                embeddedDir = path.toPath()
            ),
            manifestUrl = manifestUrl,
            data = data,
        )
    }

    override fun stop() {
        ziplineExecutorService.shutdown()
    }
}