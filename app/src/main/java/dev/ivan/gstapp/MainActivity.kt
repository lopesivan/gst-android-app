package dev.ivan.gstapp

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class MainActivity : Activity()
{
    external fun nativeGetGStreamerInfo(): String

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.textView).text = nativeGetGStreamerInfo()
    }

    companion object
    {
        init
        {
            System.loadLibrary("gstreamer_android")
            System.loadLibrary("gst-android-app")
        }
    }
}
