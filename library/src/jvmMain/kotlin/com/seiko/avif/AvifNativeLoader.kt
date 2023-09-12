package com.seiko.avif

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption.REPLACE_EXISTING
import java.util.Locale.US

@Suppress("UnsafeDynamicallyLoadedCode") // Only loading from our own JAR contents.
internal actual fun loadNativeLibrary() {
    val osName = System.getProperty("os.name").lowercase(US)
    val osArch = System.getProperty("os.arch").lowercase(US)
    val nativeLibraryJarPath = when {
        osName.contains("linux") -> "/jni/$osArch/lib$jni_prefix.so"
        osName.contains("mac") -> "/jni/$osArch/lib$jni_prefix.dylib"
        else -> throw IllegalStateException("Unsupported OS: $osName")
    }
    val nativeLibraryUrl = AvifDecoder::class.java.getResource(nativeLibraryJarPath)
        ?: throw IllegalStateException("Unable to read $nativeLibraryJarPath from JAR")
    val nativeLibraryFile: Path
    try {
        nativeLibraryFile = Files.createTempFile(jni_prefix, null)

        // File-based deleteOnExit() uses a special internal shutdown hook that always runs last.
        nativeLibraryFile.toFile().deleteOnExit()
        nativeLibraryUrl.openStream().use { nativeLibrary ->
            Files.copy(nativeLibrary, nativeLibraryFile, REPLACE_EXISTING)
        }
    } catch (e: IOException) {
        throw RuntimeException("Unable to extract native library from JAR", e)
    }
    System.load(nativeLibraryFile.toAbsolutePath().toString())
}

private const val jni_prefix = "avif-kmp"
