package com.seiko.avif.internal

internal actual fun log(level: String, throwable: Throwable?, message: () -> String) {
    if (throwable != null) {
        println("AvifKmp[$level]: ${message()}\n${throwable.stackTraceToString()}")
    } else {
        println("AvifKmp[$level]: ${message()}")
    }
}
