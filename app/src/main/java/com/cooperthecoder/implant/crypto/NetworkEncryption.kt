package com.cooperthecoder.implant.crypto

import android.util.Base64
import org.libsodium.jni.NaCl
import org.libsodium.jni.Sodium

class NetworkEncryption(val serverPublicKey: ByteArray) {

    constructor(serverPublicKey: String) : this(Base64.decode(serverPublicKey, Base64.DEFAULT)) {
    }

    fun encrypt(plaintext: String): String {
        val encoded = Base64.encodeToString(encrypt(plaintext.toByteArray()), Base64.DEFAULT)
        return encoded
    }

    private fun encrypt(plaintext: ByteArray): ByteArray {
        NaCl.sodium()
        val ciphertext = ByteArray(ciphertextLength(plaintext))
        Sodium.crypto_box_seal(ciphertext, plaintext, plaintext.size, serverPublicKey)
        return ciphertext
    }

    private fun ciphertextLength(plaintext: ByteArray): Int = Sodium.crypto_box_sealbytes() + plaintext.size
}