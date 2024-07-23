package com.santimattius.kmp.core

import app.cash.zipline.loader.DefaultFreshnessCheckerNotFresh
import app.cash.zipline.loader.LoadResult
import app.cash.zipline.loader.ZiplineLoader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

private const val ApplicationName = "zipline-example"

fun startZipline(
    scope: CoroutineScope,
    dispatcher: CoroutineDispatcher,
    loader: ZiplineLoader,
    manifestUrl: String,
    data: MutableStateFlow<String>
) {

    scope.launch(dispatcher + SupervisorJob()) {
        // load the manifest
        val loadResultFlow: Flow<LoadResult> = loader.load(
            applicationName = ApplicationName,
            freshnessChecker = DefaultFreshnessCheckerNotFresh,
            manifestUrlFlow = repeatFlow(manifestUrl, 500L),
        )
        var previousJob: Job? = null
        loadResultFlow.collect { result ->
            previousJob?.cancel()
            if (result is LoadResult.Success) {
                val zipline = result.zipline
                //take the service
                val service = zipline.take<SayHelloService>("SayHelloService")

                val job = launch {
                    //emit the data
                    data.emitAll(service.sayHello())
                }

                job.invokeOnCompletion {
                    service.close()
                    zipline.close()
                }

                previousJob = job
            }
        }
    }
}

/** Poll for code updates by emitting the manifest on an interval. */
private fun <T> repeatFlow(content: T, delayMillis: Long): Flow<T> {
    return flow {
        while (true) {
            emit(content)
            delay(delayMillis)
        }
    }
}