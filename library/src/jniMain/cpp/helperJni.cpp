#include <jni.h>
#include <stddef.h>
#include <cstdio>

#define MSG_SIZE 1024

/**
 * Instructs the JNI environment to throw an exception.
 *
 * @param pEnv JNI environment
 * @param szClassName class name to throw
 * @param szFmt sprintf-style format string
 * @param ... sprintf-style args
 * @return 0 on success; a negative value on failure
 */
jint throwException(
        JNIEnv* pEnv,
        const char* szClassName,
        const char* szFmt,
        va_list va_args) {
    char szMsg[MSG_SIZE];
    vsnprintf(szMsg, MSG_SIZE, szFmt, va_args);
    jclass exClass = pEnv->FindClass(szClassName);
    return pEnv->ThrowNew(exClass, szMsg);
}

/**
 * Instructs the JNI environment to throw an IllegalStateException.
 *
 * @param pEnv JNI environment
 * @param szFmt sprintf-style format string
 * @param ... sprintf-style args
 * @return 0 on success; a negative value on failure
 */
jint throwIllegalStateException(JNIEnv* pEnv, const char* szFmt, ...) {
    va_list va_args;
    va_start(va_args, szFmt);
    jint ret =
            throwException(pEnv, "java/lang/IllegalStateException", szFmt, va_args);
    va_end(va_args);
    return ret;
}
