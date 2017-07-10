package com.cooperthecoder.implant.crypto

import android.util.Base64
import org.libsodium.jni.encoders.Encoder


class Base64Encoder: Encoder {

    companion object {
        val encoder = Base64Encoder()

        fun encode(bytes: ByteArray): String {
            return encoder.encode(bytes)
        }

        fun decode(string: String): ByteArray {
            return encoder.decode(string)
        }
    }

    override fun encode(bytes: ByteArray): String {
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }

    override fun decode(string: String): ByteArray {
        return Base64.decode(string, Base64.DEFAULT)
    }
}