package com.cooperthecoder.implant.data

import android.os.Environment
import java.io.File

object UploadQueue {

    const val UPLOAD_QUEUE_NAME = "uploads"

    private val file = File(Environment.getDataDirectory(), UPLOAD_QUEUE_NAME)

    fun push(filename: String) {
        file.appendText("$filename\n")
    }

    fun files(): List<String> {
        return file.readLines()
    }

    fun clear(): Boolean {
        return file.delete()
    }
}