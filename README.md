Update
======
Google made the BIND_ACCESSIBILITY_SERVICE only available to system apps.
This attack no longer works.
Feel free to get it working ;).

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

Overlay Capabilities
====================
+ Determine the position of our AccessibilityService in the Settings Menu dynamically
+ Clickjacking -> a11y working on some screen sizes


Disclaimer
==========
I am not responsible for anything illegal, immoral or stupid you do with this code.
Please use it for educational purposes only.
