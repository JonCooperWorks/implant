package com.cooperthecoder.implant.model

import org.json.JSONObject
import java.util.*

class Command private constructor(val command: String, val arguments: Map<String, String>) {
    val nonce = UUID.randomUUID().toString()

    companion object {
        const val FIELD_NAME_COMMAND = "command"
        const val FIELD_NAME_ARGUMENTS = "arguments"

        fun fromJson(text: String): Command {
            val json = JSONObject(text)
            val arguments = hashMapOf<String, String>()
            val argumentsJSONObject = json.getJSONObject(FIELD_NAME_ARGUMENTS)
            for (key in argumentsJSONObject.keys()) {
                arguments.put(key, argumentsJSONObject.getString(key))
            }
            val command = Command.Builder()
                    .command(json.getString(FIELD_NAME_COMMAND))
                    .arguments(arguments)
                    .build()
            return command
        }
    }

    class Builder {
        private var command: String? = null
        private var arguments: Map<String, String>? = null

        fun command(command: String): Builder {
            this.command = command
            return this
        }

        fun arguments(arguments: Map<String, String>): Builder {
            this.arguments = arguments
            return this
        }

        fun build(): Command {
            return Command(command!!, arguments!!)
        }
    }

    fun json(): String {
        val json = JSONObject()
        json.put(FIELD_NAME_COMMAND, command)
        json.put(FIELD_NAME_ARGUMENTS, JSONObject(arguments))
        return json.toString()
    }
}