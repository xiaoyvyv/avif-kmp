package com.seiko.avif

internal actual fun loadNativeLibrary() {
    System.loadLibrary("avif-android")
}
