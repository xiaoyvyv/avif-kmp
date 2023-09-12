#include "util-android.h"

void jniThrowException(JNIEnv *env, const char *className, const char *msg) {
    jclass clazz = (*env)->FindClass(env, className);
    if (!clazz) {
        ALOGE("Unable to find exception class %s", className);
        /* ClassNotFoundException now pending */
        return;
    }
    if ((*env)->ThrowNew(env, clazz, msg) != JNI_OK) {
        ALOGE("Failed throwing '%s' '%s'", className, msg);
        /* an exception, most likely OOM, will now be pending */
    }
    (*env)->DeleteLocalRef(env, clazz);
}
