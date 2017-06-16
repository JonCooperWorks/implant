package com.cooperthecoder.implant

import android.content.Context
import android.content.SharedPreferences


object SharedPreferencesQuery {
    private fun reader(context: Context): SharedPreferences {
        val appContext = context.applicationContext
        val prefs = appContext.getSharedPreferences(
                Config.SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE
        )
        return prefs
    }

    private fun writer(context: Context): SharedPreferences.Editor {
        return reader(context).edit()
    }

    fun updateScreenState(context: Context, screenState: String, timestamp: Long): Boolean {
        val added = writer(context).putString(Config.PREFS_KEY_SCREEN_STATE, screenState)
                .putLong(Config.PREFS_KEY_STATE_TIMESTAMP, timestamp)
                .commit()
        return added
    }

    fun getScreenState(context: Context): String {
        // Assume the screen is on if we don't know.
        val screenState = reader(context).getString(Config.PREFS_KEY_SCREEN_STATE, ScreenState.ON)
        return screenState
    }

    fun addLastPinEntered(context: Context, pin: String): Boolean {
        val added = writer(context).putString(Config.PREFS_KEY_LAST_PIN, pin)
                .commit()
        return added
    }

    fun getNextPin(context: Context): String? {
        val nextPin = reader(context).getString(Config.PREFS_KEY_LAST_PIN, null)
        return nextPin
    }
}