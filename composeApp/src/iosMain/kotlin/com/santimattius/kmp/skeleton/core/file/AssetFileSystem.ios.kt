package com.santimattius.kmp.skeleton.core.file

import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

actual class AssetFileSystem {
    actual fun readFile(assetPath: String): Path {
        return assetPath.toPath()
    }

    actual val fileSystem: FileSystem
        get() = FileSystem.SYSTEM
}