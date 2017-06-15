package com.cooperthecoder.implant

import android.content.Context


object SharedPreferencesQuery {
    fun updateScreenState(context: Context, screenState: String, timestamp: Long): Boolean {
        val appContext = context.applicationContext
        val editor = appContext.getSharedPreferences(
                Config.SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE
        ).edit()
        val added = editor.putString(Config.PREFS_KEY_SCREEN_STATE, screenState)
                .putLong(Config.PREFS_KEY_STATE_TIMESTAMP, timestamp)
                .commit()
        return added
    }

    fun getScreenState(context: Context): String {
        val appContext = context.applicationContext
        val prefs = appContext.getSharedPreferences(
                Config.SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE
        )
        // Assume the screen is on if we don't know.
        val screenState = prefs.getString(Config.PREFS_KEY_SCREEN_STATE, ScreenState.ON)
        return screenState
    }

    fun addLastPinEntered(context: Context, pin: String): Boolean {
        val appContext = context.applicationContext
        val editor = appContext.getSharedPreferences(
                Config.SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE
        ).edit()
        val added = editor.putString(Config.PREFS_KEY_LAST_PIN, pin)
                .commit()
        return added
    }

    fun getNextPin(context: Context): String? {
        val appContext = context.applicationContext
        val prefs = appContext.getSharedPreferences(
                Config.SHARED_PREFERENCES_NAME,
                Context.MODE_PRIVATE
        )
        val nextPin = prefs.getString(Config.PREFS_KEY_LAST_PIN, null)
        return nextPin
    }
}