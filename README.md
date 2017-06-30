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
+ Unlock login screen
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
+ Automatic keystroke uploads when user is sleeping and connected to Wi-Fi
+ Execute shell commands
+ Download payload from URL
+ Add upload job to queue

Push Service
------------
Since Google doesn't let us silently receive background messages (for obvious reasons),
we'll have to do it ourselves using the JobScheduler API to poll for commands when
the device is charging and connected to Wi-Fi.
This means that we'll be giving up real time communication with the device.

Disclaimer
==========
I am not responsible for anything illegal, immoral or stupid you do with this code.
Please use it for educational purposes only.