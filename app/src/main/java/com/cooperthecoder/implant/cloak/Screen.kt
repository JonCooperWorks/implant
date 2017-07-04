/*
* This object is meant to provide a convenient way to get sizes based on the size of default Android
* UI components such as the ActionBar and ListView items.
* */
package com.cooperthecoder.implant.cloak

import android.content.Context
import android.util.TypedValue
import com.cooperthecoder.implant.Config


object Screen {

    fun actionBarHeight(context: Context): Int {
        val tv = TypedValue()
        var height = Config.ACCESSIBILITY_SETTINGS_TOP_PADDING
        if (context.theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            height = TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
        }
        return height
    }

    fun listViewItemHeight(context: Context): Int {
        val tv = TypedValue()
        var height = Config.ACCESSIBILITY_SETTINGS_HEIGHT
        if (context.theme.resolveAttribute(android.R.attr.listPreferredItemHeightLarge, tv, true)) {
            height = TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
        }
        return height
    }

    fun screenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

    fun screenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }
}