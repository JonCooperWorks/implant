package com.cooperthecoder.implant.cloak

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.support.constraint.ConstraintLayout
import android.view.Gravity
import android.view.WindowManager
import com.cooperthecoder.implant.Overlay

class DistractionStage(context: Context, listener: () -> Unit) : Stage(context, listener) {
    override fun stageOverlays(): List<Overlay> {
        val overlays = ArrayList<Overlay>()
        val overlay = ConstraintLayout(context)
        overlay.setBackgroundColor(Color.RED)

        val lpFlags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
        val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                lpFlags,
                PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.LEFT or Gravity.TOP
        params.alpha = 0.5F
        overlays.add(Overlay(overlay, params))
        return overlays
    }
}