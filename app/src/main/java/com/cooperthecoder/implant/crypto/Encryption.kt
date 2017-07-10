package com.cooperthecoder.implant.crypto

interface Encryption {
    open class CryptoException(e: String) : Exception(e)
    class DecryptionException(e: String) : CryptoException(e)
    class EncryptionException(e: String) : CryptoException(e)

    fun encrypt(plaintext: ByteArray): ByteArray
    fun decrypt(ciphertext: ByteArray): ByteArray
}