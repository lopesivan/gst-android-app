#include <jni.h>
#include <gst/gst.h>
#include <mutex>
#include <string>

namespace
{
std::mutex pipelineMutex;
GstElement* pipeline = nullptr;

jstring toJString(JNIEnv* env, const std::string& text)
{
    return env->NewStringUTF(text.c_str());
}

void stopPipeline()
{
    if (pipeline == nullptr)
        return;

    gst_element_set_state(pipeline, GST_STATE_NULL);
    gst_object_unref(pipeline);
    pipeline = nullptr;
}
}

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

extern "C"
JNIEXPORT jstring JNICALL
Java_dev_ivan_gstapp_MainActivity_nativeStart(JNIEnv* env, jobject /* this */)
{
    std::lock_guard lock(pipelineMutex);
    stopPipeline();

    GError* error = nullptr;
    pipeline = gst_parse_launch(
        "audiotestsrc wave=sine freq=440 ! audioconvert ! "
        "audioresample ! openslessink",
        &error);

    if (pipeline == nullptr || error != nullptr) {
        const std::string message = error != nullptr
            ? "Erro no pipeline: " + std::string(error->message)
            : "Erro: o pipeline não foi criado.";
        g_clear_error(&error);
        stopPipeline();
        return toJString(env, message);
    }

    const GstStateChangeReturn result =
        gst_element_set_state(pipeline, GST_STATE_PLAYING);
    if (result == GST_STATE_CHANGE_FAILURE) {
        stopPipeline();
        return toJString(env, "Erro: não foi possível iniciar o pipeline.");
    }

    return toJString(env,
        "Pipeline em execução: senoide de 440 Hz → OpenSL ES");
}

extern "C"
JNIEXPORT jstring JNICALL
Java_dev_ivan_gstapp_MainActivity_nativeStop(JNIEnv* env, jobject /* this */)
{
    std::lock_guard lock(pipelineMutex);
    stopPipeline();
    return toJString(env, "Pipeline parado.");
}
