package com.cooperthecoder.implant

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class SleepDetectingReceiver : BroadcastReceiver() {


    companion object {
        private val TAG = SleepDetectingReceiver::class.java.name
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "Checking if user is asleep")
        val prefs = context.getSharedPreferences(Config.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val screenState = prefs.getString(Config.PREFS_KEY_SCREEN_STATE, "")
        val timestamp = prefs.getLong(Config.PREFS_KEY_STATE_TIMESTAMP, 0L)
        if (screenState == "" || timestamp == 0L) {
            return
        }
        val elapsed = System.currentTimeMillis() - timestamp
        if (screenState == ScreenState.OFF && elapsed >= Config.VICTIM_SLEEPING_INTERVAL) {
            // Unlock the screen and do things.
            Log.d(TAG, "User is asleep.")
        } else {
            // This shouldn't happen but in case it does, handle it
            Log.d(TAG, "User is awake.")
        }
    }
}
