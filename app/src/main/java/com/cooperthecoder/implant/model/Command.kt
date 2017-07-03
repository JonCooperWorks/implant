package com.cooperthecoder.implant.model

import java.util.*

class Command private constructor(val command: String, val arguments: Map<String, Any>) {
    val nonce = UUID.randomUUID().toString()

    class Builder {
        var command: String? = null
        var arguments: Map<String, Any>? = null

        fun command(command: String): Builder {
            this.command = command
            return this
        }

        fun arguments(arguments: Map<String, Any>): Builder {
            this.arguments = arguments
            return this
        }

        fun build(): Command {
            return Command(command!!, arguments!!)
        }
    }
}