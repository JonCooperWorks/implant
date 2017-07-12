package com.cooperthecoder.implant.crypto

import org.libsodium.jni.Sodium
import org.libsodium.jni.keys.KeyPair


class Base64KeyPair {

    companion object {
        private const val SEED_SIZE = 32
    }

    val keypair: KeyPair

    constructor(keypair: KeyPair? = null) {
        if (keypair == null) {
            val seed = ByteArray(SEED_SIZE)
            Sodium.randombytes(seed, seed.size)
            this.keypair = KeyPair(seed)
        } else {
            this.keypair = keypair
        }
    }

    constructor(privateKey: String) {
        this.keypair = KeyPair(privateKey, Base64Encoder.encoder)
    }


    fun privateKeyBase64(): String {
        return Base64Encoder.encode(keypair.privateKey.toBytes())
    }

    fun publicKeyBase64(): String {
        return Base64Encoder.encode(keypair.publicKey.toBytes())
    }
}