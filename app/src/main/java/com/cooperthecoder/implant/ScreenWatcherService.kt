package com.cooperthecoder.implant

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import java.text.DateFormat
import java.util.*

class ScreenWatcherService : IntentService(TAG) {

    companion object {
        private val TAG = ScreenWatcherService::class.java.name

        const val EXTRA_TIMESTAMP = "timestamp"
        const val EXTRA_SCREEN_STATE = "screen_state"
    }

    override fun onHandleIntent(intent: Intent) {
        val screenState = intent.getStringExtra(EXTRA_SCREEN_STATE)
        val timestamp = intent.getLongExtra(EXTRA_TIMESTAMP, 0)
        updateScreenState(screenState, timestamp)
    }

    private fun formatDate(timeMillis: Long) = DateFormat.getDateTimeInstance().format(Date(timeMillis))

    private fun updateScreenState(screenState: String, timestamp: Long) {
        val prefs = getSharedPreferences(Config.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
        prefs.putString(Config.PREFS_KEY_SCREEN_STATE, screenState)
                .putLong(Config.PREFS_KEY_STATE_TIMESTAMP, timestamp)
                .commit()

        val changeTime = formatDate(timestamp)
        Log.d(TAG, "Screen state changed at $changeTime to $screenState")
    }
}
