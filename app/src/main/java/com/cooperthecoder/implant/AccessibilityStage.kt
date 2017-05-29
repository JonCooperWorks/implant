package com.cooperthecoder.implant

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager

class AccessibilityStage(context: Context, listener: () -> Unit) : Stage(context, listener), View.OnTouchListener {
    val TAG: String = javaClass.name

    var topOverlay: View? = null
    var bottomOverlay: View? = null

    override fun drawOnScreen() {
        topOverlay = ConstraintLayout(context)
        bottomOverlay = ConstraintLayout(context)

        Log.d(TAG, "Overlay created")
        topOverlay!!.setBackgroundColor(Color.RED)
        bottomOverlay!!.setBackgroundColor(Color.BLUE)

        val lpFlags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
        val topParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                640,
                WindowManager.LayoutParams.TYPE_PHONE,
                lpFlags,
                PixelFormat.TRANSLUCENT
        )
        topParams.gravity = Gravity.LEFT or Gravity.TOP
        topParams.alpha = 0.5F
        addOverlay(topOverlay!!, topParams)

        val bottomParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                900,
                WindowManager.LayoutParams.TYPE_PHONE,
                lpFlags,
                PixelFormat.TRANSLUCENT
        )
        bottomParams.gravity = Gravity.LEFT or Gravity.BOTTOM
        bottomParams.alpha = 0.5F
        addOverlay(bottomOverlay!!, bottomParams)
    }

    override fun clearFromScreen() {
        removeOverlay(topOverlay)
        removeOverlay(bottomOverlay)
    }


}