package com.santimattius.kmp.core

import app.cash.zipline.loader.ManifestVerifier.Companion.NO_SIGNATURE_CHECKS
import app.cash.zipline.loader.ZiplineLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import platform.Foundation.NSURLSession

// TODO: "http://localhost:8080/manifest.zipline.json"
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
            ),
            manifestUrl = manifestUrl,
            data = data,
        )
    }

    override fun stop() {}
}