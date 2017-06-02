package com.cooperthecoder.implant

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log

class ScreenWatcherService : Service() {

    companion object {
        private val TAG = ScreenWatcherService::class.java.name

        @JvmStatic val EXTRA_TIMESTAMP = "timestamp"
        @JvmStatic val EXTRA_SCREEN_STATE = "screen_state"
        @JvmStatic val STATE_SCREEN_ON = "screen_on"
        @JvmStatic val STATE_SCREEN_OFF = "screen_off"
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val screenState = intent.getStringExtra(EXTRA_SCREEN_STATE)
        val timestamp = intent.getStringExtra(EXTRA_TIMESTAMP)
        updateScreenState(screenState, timestamp)
        return START_NOT_STICKY
    }

    private fun updateScreenState(screenState: String, timestamp: String) {
        val prefs = getSharedPreferences(Config.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
        prefs.putString(Config.PREFS_KEY_SCREEN_STATE, screenState)
                .putString(Config.PREFS_KEY_STATE_TIMESTAMP, timestamp)
                .commit()
        Log.d(TAG, "Screen state changed at $timestamp to $screenState")
    }
}
