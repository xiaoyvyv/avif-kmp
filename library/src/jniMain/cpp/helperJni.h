#include <jni.h>

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
        va_list va_args);

/**
 * Instructs the JNI environment to throw a IllegalStateException.
 *
 * @param pEnv JNI environment
 * @param szFmt sprintf-style format string
 * @param ... sprintf-style args
 * @return 0 on success; a negative value on failure
 */
jint throwIllegalStateException(JNIEnv* pEnv, const char* szFmt, ...);
