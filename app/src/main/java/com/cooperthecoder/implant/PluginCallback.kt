package com.cooperthecoder.implant

import java.io.File

interface PluginCallback {
    fun onPluginDownloaded(file: File)
    fun onDownloadFailed(cause: Throwable)
}