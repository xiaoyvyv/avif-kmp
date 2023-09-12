package com.seiko.avif.internal

import android.util.Log

internal actual fun log(level: String, throwable: Throwable?, message: () -> String) {
    when (level) {
        "warn" -> Log.w("AvifDecoder", message(), throwable)
        "error" -> Log.e("AvifDecoder", message(), throwable)
        else -> Log.i("AvifDecoder", message(), throwable)
    }
}
