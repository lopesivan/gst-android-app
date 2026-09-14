#include <jni.h>
#include <gst/gst.h>

extern "C"
JNIEXPORT jstring JNICALL
Java_dev_ivan_gstapp_MainActivity_nativeGetGStreamerInfo(JNIEnv *env, jobject /* this */)
{
    gst_init(nullptr, nullptr);

    gchar *version_utf8 = gst_version_string();
    jstring version_jstring = env->NewStringUTF(version_utf8);
    g_free(version_utf8);

    return version_jstring;
}
