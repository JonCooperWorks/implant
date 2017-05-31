Cloak and Dagger Implementation
===============================
Implant is an (unfinished) implementation of the Android Clock and Dagger vulnerability.
To learn more about Cloak and Dagger see, http://cloak-and-dagger.org.

If the overlays do not display correctly on your phone, adjust the overlay dimensions in the
relevant Stage subclasses.
Currently only the AccessibilityStage of the attack uses Android side channels to determine the
position of elements on screen.

Pull requests and criticism accepted.

Installing Implant
==================
To install Implant, import this code into Android Studio.
Since Android only grants the SYSTEM_ALERT_WINDOW permission to apps from the Play Store, you'll
have to manually enable the permission and press the back button to begin the attack, or publish
it to the Play Store.
Implant uses coloured translucent overlays to show exactly what is going on in the background.
Simply click the holes in the overlay to progress the attack.
It has been tested in the Android emulator Nexus 5X running Marshmallow and Nougat.

Accessibility Service Capabilities
==================================
Done
----
Log keystrokes
Log browser history
Log selected text
Log applications used

To Do
-----
Log login PIN
Dropper mechanism for installing second stage payload

Disclaimer
==========
I am not responsible for anything illegal, immoral or stupid you do with this code.
Please use it for educational purposes only.

TODO
====
+ Make the overlays dynamically resize based on screen size
+ Implement all attacks described in the paper
