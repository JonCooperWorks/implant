package com.cooperthecoder.implant.data

import android.content.Context
import android.content.SharedPreferences
import com.cooperthecoder.implant.Config


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