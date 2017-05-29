package com.cooperthecoder.implant

import android.content.Context
import android.view.WindowManager


class RedressingAttack(val stages: List<Stage>) {

    private var currentStage: Stage? = null
    private var attackIndex = -1

    private fun moveToStage(context: Context, windowManager: WindowManager, index: Int) {
        val stage = stages.getOrNull(index)
        if (stage != null) {
            stage.drawOnScreen(context, windowManager)
            currentStage = stage
            attackIndex = index
        }
    }

    fun moveToNext(context: Context, windowManager: WindowManager): Boolean {
        currentStage?.clearFromScreen(context, windowManager)
        val next = attackIndex + 1
        if (next < stages.size) {
            moveToStage(context, windowManager, next)
            return true
        }

        return false
    }

    fun stopAttack(context: Context, windowManager: WindowManager) {
        currentStage?.clearFromScreen(context, windowManager)
    }

    interface Listener {
        fun onAttackDone()
    }
}