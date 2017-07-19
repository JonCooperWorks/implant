package com.cooperthecoder.implant.command

import android.util.Log
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken

class CommandSubscriptionCallback : IMqttActionListener {

    companion object {
        val TAG = CommandSubscriptionCallback::class.java.name
    }
    override fun onSuccess(token: IMqttToken) {
        Log.d(TAG, "Successfully subscribed to command channel.")
    }

    override fun onFailure(token: IMqttToken, cause: Throwable) {
        cause.printStackTrace()
    }

}