package com.santimattius.kmp.skeleton.core.file

import android.content.Context
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.buffer

actual class AssetFileSystem(
    private val context: Context
) {
    actual fun readFile(assetPath: String): Path {
        val destinationPath = "${context.filesDir}/$assetPath".toPath()
        context.assets.list(assetPath)?.forEach {
            context.assets.open(it).use { inputStream ->
            fileSystem.sink(destinationPath).buffer().use { sink ->
                inputStream.copyTo(sink.outputStream())
            }
        }
        }
        return destinationPath
    }

    actual val fileSystem: FileSystem
        get() = FileSystem.SYSTEM
}