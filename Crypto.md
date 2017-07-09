Cryptography
============
Although the underlying connection must use TLS, an additional layer of encryption is needed to prevent
a rogue endpoint from subscribing to other channels and being able to see commands and responses 
sent to devices other than those they possess the key for.
This architecture also allows for separating the private key from the C&C server.
All clients are bundled with the operator's public key.
The libsodium crypto_box_easy family of functions are used to provide authenticated encryption
between client and servers.

Encryption and Decryption
------------------------
The nonce is prepended to the ciphertext before it is sent.
The first ```crypto_box_NONCEBYTES``` of the ciphertext should be read as the nonce, and the rest
passed to the platform's implementation of crypto_box_open_easy as ciphertext.

Encryption is done using the following algorithm:
```
function encrypt(peerPublicKey, plaintext)
    nonce = platformGenerateRandomBytes(size=cryptoBoxNonceSize)
    ciphertext = platformCryptoBoxEasyEncrypt(
        nonce=nonce,
        message=plaintext,
        publicKey=peerPublicKey,
        privateKey=clientPrivateKey
    )
    return nonce + ciphertext
```


Decryption is done using the following algorithm:
```
function decrypt(peerPublicKey, nonceAndCiphertext)
    nonce = firstNBytesOfBuffer(
        n=cryptoBoxNonceSize,
        buffer=nonceAndCiphertext
    )
    ciphertext = restOfBuffer(after=nonceAndCiphertextSize)
    plaintext = platformCryptoBoxEasyOpen(
        nonce=nonce,
        ciphertext=ciphertext,
        publicKey=peerPublicKey,
        privateKey=clientPrivateKey
    )
    return plaintext
```