package com.cooperthecoder.implant.crypto

import org.libsodium.jni.encoders.Encoder
import shaded.org.apache.commons.codec.binary.Base64


class Base64Encoder : Encoder {

    companion object {


        val encoder = Base64Encoder()

        fun encode(bytes: ByteArray): String {
            return encoder.encode(bytes)
        }

        fun decode(string: String): ByteArray {
            return encoder.decode(string)
        }
    }

    val encoder = Base64(true)

    override fun encode(bytes: ByteArray): String {
        return encoder.encodeToString(bytes)
    }

    override fun decode(string: String): ByteArray {
        return encoder.decode(string)
    }
}