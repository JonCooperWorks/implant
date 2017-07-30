package com.cooperthecoder.implant

import android.support.test.runner.AndroidJUnit4
import com.cooperthecoder.implant.crypto.NetworkEncryption
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.libsodium.jni.NaCl
import org.libsodium.jni.Sodium
import org.hamcrest.Matchers.*
import org.junit.Assert.*


@RunWith(AndroidJUnit4::class)
class NetworkEncryptionTest {

    @Test
    fun encryptsAndDecryptsCorrectly() {
        NaCl.sodium()
        val public = ByteArray(Sodium.crypto_box_publickeybytes())
        val private = ByteArray(Sodium.crypto_box_secretkeybytes())
        Sodium.crypto_box_keypair(public, private)
        val message = "Hello world"
        val ne = NetworkEncryption(public, private)
        val ciphertext = ne.encrypt(message)
        assertThat(message, not(equalTo(ciphertext)))
        val plaintext = ne.decrypt(ciphertext)
        assertThat(plaintext, equalTo(message))
    }
}