package com.cooperthecoder.implant

import android.app.Service
import android.content.Intent
import android.os.IBinder

class CameraService: Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
    }
}