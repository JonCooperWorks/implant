package com.cooperthecoder.implant

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import java.text.DateFormat
import java.util.*

class ScreenStateReceiver : BroadcastReceiver() {

    companion object {
        private val TAG = ScreenStateReceiver::class.java.name
    }

    override fun onReceive(context: Context, intent: Intent) {
        val screenState = when (intent.action) {
            Intent.ACTION_SCREEN_ON -> {
                ScreenWatcherService.STATE_SCREEN_ON
            }

            Intent.ACTION_SCREEN_OFF -> {
                ScreenWatcherService.STATE_SCREEN_OFF
            }

            else -> {
                throw IllegalStateException("This should never happen.")
            }
        }

        // Record the time the screen turned changed state.
        val timestamp = DateFormat.getDateTimeInstance().format(Date())
        val updateService = Intent(context, ScreenWatcherService::class.java)
        updateService.putExtra(ScreenWatcherService.EXTRA_TIMESTAMP, timestamp)
        updateService.putExtra(ScreenWatcherService.EXTRA_SCREEN_STATE, screenState)
        context.startService(updateService)
        Log.d(TAG, "Service started to update screen state.")
    }
}
