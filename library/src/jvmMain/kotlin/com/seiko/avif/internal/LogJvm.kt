package com.seiko.avif.internal

import com.seiko.avif.AvifDecoder
import java.util.logging.Level
import java.util.logging.Logger

private val logger = Logger.getLogger(AvifDecoder::class.qualifiedName)

internal actual fun log(level: String, throwable: Throwable?, message: () -> String) {
    when (level) {
        "warn" -> logger.log(Level.WARNING, message(), throwable)
        "error" -> logger.log(Level.SEVERE, message(), throwable)
        else -> logger.log(Level.INFO, message(), throwable)
    }
}
