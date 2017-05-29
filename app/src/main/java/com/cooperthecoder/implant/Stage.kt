package com.cooperthecoder.implant

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager

abstract class Stage(var listener: RedressingAttack.Listener?): View.OnTouchListener {
    companion object {
        val TAG = javaClass.name
    }
    abstract fun drawOnScreen(context: Context, windowManager: WindowManager)
    abstract fun clearFromScreen(context: Context, windowManager: WindowManager)

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        Log.d(TAG, "Touch coordinates: ($x, $y)")
        if (x == 0.0F && y == 0.0F) {
            listener?.onAttackDone()
        }
        return false
    }

    protected fun addOverlay(overlay: View, windowManager: WindowManager, params: WindowManager.LayoutParams) {
        if (overlay.windowToken == null) {
            Log.d(TAG, "Adding overlay to WindowManager")
            try {
                windowManager.addView(overlay, params)
                Log.d(TAG, "Overlay added to WindowManager")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        Log.d(TAG, "Overlay already attached to WindowManager")
    }

    protected fun removeOverlay(overlay: View?, windowManager: WindowManager) {
        if (overlay?.windowToken != null) {
            windowManager.removeView(overlay)
        }
    }
}