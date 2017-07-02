package com.cooperthecoder.implant.data

import android.content.Context
import android.provider.Settings

object DeviceProperties {
    fun deviceId(context: Context): String {
        val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        return deviceId
    }
}