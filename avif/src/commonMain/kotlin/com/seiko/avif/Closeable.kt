package com.seiko.avif

expect interface Closeable {
    fun close()
}

inline fun <T : Closeable?, R> T.use(block: (T) -> R): R {
    var result: R? = null
    var thrown: Throwable? = null

    try {
        result = block(this)
    } catch (t: Throwable) {
        thrown = t
    } finally {
        try {
            this?.close()
        } catch (t: Throwable) {
            if (thrown == null) {
                thrown = t
            } else {
                thrown.addSuppressed(t)
            }
        }
    }

    if (thrown != null) throw thrown
    return result!!
}
