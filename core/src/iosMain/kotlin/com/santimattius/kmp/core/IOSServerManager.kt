package com.santimattius.kmp.core

import app.cash.zipline.loader.ManifestVerifier.Companion.NO_SIGNATURE_CHECKS
import app.cash.zipline.loader.ZiplineCache
import app.cash.zipline.loader.ZiplineLoader
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import okio.FileSystem
import okio.Path.Companion.toPath
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURLSession
import platform.Foundation.NSUserDomainMask

class IOSServerManager(
    private val manifestUrl: String,
    override val scope: CoroutineScope
) : ServerManager {

    private val ziplineDispatcher = Dispatchers.Main
    private val urlSession = NSURLSession.sharedSession

    override val data = MutableStateFlow("")

    override fun start() {
        startZipline(
            scope = scope,
            dispatcher = ziplineDispatcher,
            loader = ZiplineLoader(
                dispatcher = ziplineDispatcher,
                manifestVerifier = NO_SIGNATURE_CHECKS,
                urlSession = urlSession,
            ).withCache(
                cache = ZiplineCache(
                    fileSystem = FileSystem.SYSTEM,
                    directory = documentDirectory().toPath(),
                    maxSizeInBytes = 10 * 1024
                )
            ),
            manifestUrl = manifestUrl,
            data = data,
        )
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return requireNotNull(documentDirectory?.path)
    }

    override fun stop() {}
}