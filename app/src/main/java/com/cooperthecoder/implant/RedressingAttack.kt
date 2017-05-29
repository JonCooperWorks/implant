package com.cooperthecoder.implant


class RedressingAttack(val stages: List<Stage>) {

    private var currentStage: Stage? = null
    private var attackIndex = -1

    private fun moveToStage(index: Int) {
        val stage = stages.getOrNull(index)
        if (stage != null) {
            stage.drawOnScreen()
            currentStage = stage
            attackIndex = index
        }
    }

    fun moveToNext(): Boolean {
        currentStage?.clearFromScreen()
        val next = attackIndex + 1
        if (next < stages.size) {
            moveToStage(next)
            return true
        }

        return false
    }

    fun stopAttack() {
        currentStage?.clearFromScreen()
    }
}