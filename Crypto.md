Cryptography
============
Although the underlying MQTT connection must use TLS, an additional layer of encryption is needed to prevent
a rogue endpoint from subscribing to other channels and being able to see commands and responses 
sent to devices other than those they possess the key for.
This architecture also allows for separating the private key from the C&C server.
All clients are bundled with the operator's public key.
All messages are Base 64 encoded before being sent on the wire.
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
    nonce = platformFirstNBytesOfBuffer(
        n=cryptoBoxNonceSize,
        buffer=nonceAndCiphertext
    )
    ciphertext = platformRestOfBuffer(after=nonceAndCiphertextSize)
    plaintext = platformCryptoBoxEasyOpen(
        nonce=nonce,
        ciphertext=ciphertext,
        publicKey=peerPublicKey,
        privateKey=clientPrivateKey
    )
    return plaintext
```

Sending is done as:
```
function sendToServer(plaintext)
    ciphertext = encrypt(
        peerPublicKey=serverPublicKey,
        plaintext=plaintext
    )
    platformSendToServerViaNetwork(ciphertext)
```

And receiving should look like:
```
function receiveFromClient(ciphertext)
    message = decrypt(
        peerPublicKey=clientPublicKey,
        nonceAndCiphertext=ciphertext
    )
    platformProcessMessage(message)
```


Topics
------
The use of a MQTT broker as the C&C adds flexibility to the malware.
Any node in possession of the operator key can act as the C&C by
subscribing to the *Replies* topic and publishing commands signed with
the operator key and encrypted with a client's public key.
It also ensures that compromising the C&C will yield no useful
information.
The operator node should subscribe to the broker's new client topic and
take note of nodes that have subscribed and unsubscribed in order to
send messages to them.

###Commands
The commands topic, `commands/`, is to be used to send commands to
clients.
To send a command to a client, the operator must sign a command with
her private key and encrypt it with the public key provided by the
client, then publish it on their client-specific channel.
Clients must disregard any message from the commands topic that is not
signed by the operator's key.

###Replies
The replies topic, `replies/`, is used to send the output of commands to
the operator.
To send a message to the operator, the client must sign its reply with
its private key generated on device, then encrypt it with the operator's
public key.
Messages that are not signed by the client's public key should be
discarded.
To eliminate the need for managing changing keys, clients should use
their public key as a client ID.