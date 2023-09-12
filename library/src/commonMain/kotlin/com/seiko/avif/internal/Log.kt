package com.seiko.avif.internal

internal expect fun log(level: String, throwable: Throwable? = null, message: () -> String)
