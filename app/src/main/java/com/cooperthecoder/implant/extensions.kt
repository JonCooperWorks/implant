package com.cooperthecoder.implant

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import android.view.accessibility.AccessibilityEvent


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