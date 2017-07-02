package com.cooperthecoder.implant.jobs

interface Engine<T> {
    fun action(): T?
}