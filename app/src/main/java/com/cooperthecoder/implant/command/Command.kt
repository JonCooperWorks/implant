package com.cooperthecoder.implant.command

import org.json.JSONObject
import java.util.*

class Command private constructor(
        val command: String,
        val arguments: HashMap<String, String>,
        val nonce: String = UUID.randomUUID().toString()
) {

    companion object {
        const val FIELD_NAME_COMMAND = "command"
        const val FIELD_NAME_ARGUMENTS = "arguments"
        const val FIELD_NAME_NONCE = "nonce"

        fun fromJson(text: String): Command {
            val json = JSONObject(text)

            val arguments = json.getJSONObject(FIELD_NAME_ARGUMENTS)
            val command = Command.Builder()
                    .command(json.getString(FIELD_NAME_COMMAND))
                    .nonce(json.getString(FIELD_NAME_NONCE))
                    .arguments(arguments)
                    .build()
            return command
        }
    }

    class Builder {
        private var command: String? = null
        private var arguments = hashMapOf<String, String>()
        private var nonce: String? = null

        fun command(command: String): Builder {
            this.command = command
            return this
        }

        fun arguments(arguments: HashMap<String, String>): Builder {
            this.arguments = arguments
            return this
        }

        fun arguments(arguments: JSONObject): Builder {
            this.arguments = hashMapOf<String, String>()
            for (key in arguments.keys()) {
                this.arguments[key] = arguments.getString(key)
            }
            return this
        }

        fun nonce(nonce: String): Builder {
            this.nonce = nonce
            return this
        }

        fun build(): Command {
            if (nonce != null) {
                return Command(command!!, arguments, nonce!!)
            }

            return Command(command!!, arguments)
        }
    }

    fun json(): String {
        val json = JSONObject()
        json.put(FIELD_NAME_COMMAND, command)
        json.put(FIELD_NAME_ARGUMENTS, JSONObject(arguments))
        json.put(FIELD_NAME_NONCE, nonce)
        return json.toString()
    }
}