package com.santimattius.kmp.core

import app.cash.zipline.loader.ManifestVerifier
import app.cash.zipline.loader.ManifestVerifier.Companion.NO_SIGNATURE_CHECKS
import app.cash.zipline.loader.ZiplineLoader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.OkHttpClient
import okio.ByteString.Companion.decodeHex
import java.util.concurrent.Executors

class AndroidServerManager(
    private val manifestUrl: String,
    override val scope: CoroutineScope
) : ServerManager {

    private val ziplineExecutorService = Executors.newSingleThreadExecutor { Thread(it, "Zipline") }
    private val ziplineDispatcher = ziplineExecutorService.asCoroutineDispatcher()
    private val okHttpClient = OkHttpClient()

    private val verifier = ManifestVerifier.Builder()
        .addEd25519("key1","78b7468afab3bbea07b389c8375b63afa0b623c0ab800ac98e4afecc02810cf1".decodeHex())
        .addEd25519("key2","04844a04209407ee2027463d11ffc41b27277cf83d274ea71d94bc24c08b1784f6745e65e2180e1b70995de2447f2d89626d9d339de36607cbe382e937e16833f7".decodeHex())
        .build()

    override val data = MutableStateFlow("")

    override fun start() {
        startZipline(
            scope = scope,
            dispatcher = ziplineDispatcher,
            loader = ZiplineLoader(
                dispatcher = ziplineDispatcher,
                manifestVerifier = verifier,
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