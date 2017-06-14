package com.cooperthecoder.implant

import android.content.Context
import java.util.*
import kotlin.NoSuchElementException


object SharedPreferencesQuery {
    fun updateScreenState(context: Context, screenState: String, timestamp: Long): Boolean {
        val editor = context.getSharedPreferences(Config.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit()
        val added = editor.putString(Config.PREFS_KEY_SCREEN_STATE, screenState)
                .putLong(Config.PREFS_KEY_STATE_TIMESTAMP, timestamp)
                .commit()
        return added
    }

    fun getScreenState(context: Context): String {
        val prefs = context.getSharedPreferences(Config.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        // Assume the screen is on if we don't know.
        val screenState = prefs.getString(Config.PREFS_KEY_SCREEN_STATE, "screen_on")
        return screenState
    }

    fun addPinCandidate(context: Context, pin: String): Boolean {
        val prefs = context.getSharedPreferences(Config.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val pinCandidates = prefs.getStringSet(Config.PREFS_KEY_PIN_CANDIDATES, Collections.emptySet())
        val newCandidates = HashSet<String>()
        newCandidates.add(pin)
        newCandidates.addAll(pinCandidates)
        val added = editor.putStringSet(Config.PREFS_KEY_PIN_CANDIDATES, newCandidates)
                .commit()
        return added
    }

    fun getNextPin(context: Context): String? {
        val prefs = context.getSharedPreferences(Config.PREFS_KEY_PIN_CANDIDATES,Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val pinCandidates = prefs.getStringSet(Config.PREFS_KEY_PIN_CANDIDATES, Collections.emptySet())
        var nextPin: String? = null
        try {
            nextPin = pinCandidates.first()
        } catch (e: NoSuchElementException) {
            // Noop
        }
        pinCandidates.remove(nextPin)
        editor.putStringSet(Config.PREFS_KEY_PIN_CANDIDATES, pinCandidates)
        return nextPin
    }
}