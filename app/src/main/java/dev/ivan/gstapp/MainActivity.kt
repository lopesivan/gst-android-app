package dev.ivan.gstapp

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import org.freedesktop.gstreamer.GStreamer

class MainActivity : Activity()
{
    external fun nativeGetGStreamerInfo(): String
    external fun nativeStart(): String
    external fun nativeStop(): String

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        var initialized = false
        val initialization = try {
            GStreamer.init(this)
            initialized = true
            "${nativeGetGStreamerInfo()} inicializado com sucesso."
        } catch (error: Exception) {
            "Falha ao inicializar o GStreamer: ${error.message}"
        }

        setContentView(R.layout.activity_main)
        val status = findViewById<TextView>(R.id.statusText)
        status.text = initialization

        val startButton = findViewById<Button>(R.id.startButton)
        val stopButton = findViewById<Button>(R.id.stopButton)
        startButton.isEnabled = initialized
        stopButton.isEnabled = initialized

        startButton.setOnClickListener {
            status.text = nativeStart()
        }
        stopButton.setOnClickListener {
            status.text = nativeStop()
        }
    }

    override fun onStop()
    {
        runCatching { nativeStop() }
        super.onStop()
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
