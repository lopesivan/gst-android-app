/*
 * Android initialization bridge supplied by the GStreamer project.
 * Kept in the app so a clean checkout can compile without copying sources
 * from the machine-specific SDK directory.
 */
package org.freedesktop.gstreamer;

import android.content.Context;

public final class GStreamer {
    private GStreamer() {}

    private static native void nativeInit(Context context) throws Exception;

    public static void init(Context context) throws Exception {
        nativeInit(context);
    }
}
