package com.santimattius.kmp.skeleton.core.file

import okio.FileSystem
import okio.Path

expect class AssetFileSystem {
    fun readFile(assetPath: String): Path
    val fileSystem: FileSystem
}