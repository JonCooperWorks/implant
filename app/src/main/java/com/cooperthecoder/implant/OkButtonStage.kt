package com.cooperthecoder.implant

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.support.constraint.ConstraintLayout
import android.view.Gravity
import android.view.WindowManager

class OkButtonStage(context: Context, listener: () -> Unit) : Stage(context, listener) {

    override fun stageOverlays(): List<Overlay> {        val overlays = ArrayList<Overlay>()
        val topOverlay = ConstraintLayout(context)
        topOverlay.setBackgroundColor(Color.RED)

        val lpFlags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
        val topParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                1050,
                WindowManager.LayoutParams.TYPE_PHONE,
                lpFlags,
                PixelFormat.TRANSLUCENT
        )
        topParams.gravity = Gravity.LEFT or Gravity.TOP
        topParams.alpha = 0.5F

        val bottomOverlay = ConstraintLayout(context)
        bottomOverlay.setBackgroundColor(Color.BLUE)
        val bottomParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                560,
                WindowManager.LayoutParams.TYPE_PHONE,
                lpFlags,
                PixelFormat.TRANSLUCENT
        )
        bottomParams.gravity = Gravity.LEFT or Gravity.BOTTOM
        bottomParams.alpha = 0.5F

        val leftOverlay = ConstraintLayout(context)
        leftOverlay.setBackgroundColor(Color.GREEN)
        val leftParams = WindowManager.LayoutParams(
                820,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                lpFlags,
                PixelFormat.TRANSLUCENT
        )
        leftParams.gravity = Gravity.LEFT
        leftParams.alpha = 0.5F

        val rightOverlay = ConstraintLayout(context)
        rightOverlay.setBackgroundColor(Color.YELLOW)
        val rightParams = WindowManager.LayoutParams(
                120,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                lpFlags,
                PixelFormat.TRANSLUCENT
        )
        rightParams.gravity = Gravity.RIGHT
        rightParams.alpha = 0.5F

        overlays.add(Overlay(topOverlay, topParams))
        overlays.add(Overlay(bottomOverlay, bottomParams))
        overlays.add(Overlay(leftOverlay, leftParams))
        overlays.add(Overlay(rightOverlay, rightParams))
        return overlays
    }

}