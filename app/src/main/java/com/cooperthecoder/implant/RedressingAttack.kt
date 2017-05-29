package com.cooperthecoder.implant


class RedressingAttack(val stages: List<Stage>) {

    private var attackIndex = -1

    private fun moveToStage(index: Int) {
        val stage = stages.getOrNull(index)
        if (stage != null) {
            stage.drawOnScreen()
            attackIndex = index
        }
    }

    fun moveToNext(): Boolean {
        stages.getOrNull(attackIndex)?.clearFromScreen()
        val next = attackIndex + 1
        if (next < stages.size) {
            moveToStage(next)
            return true
        }

        return false
    }

    fun stopAttack() {
        stages.getOrNull(attackIndex)?.clearFromScreen()
    }
}