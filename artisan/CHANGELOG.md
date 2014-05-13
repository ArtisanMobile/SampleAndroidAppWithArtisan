====================================
   Artisan Android SDK Change Log
====================================

## Version 1.1.5

* Adds manual installation instructions. Please refer to section 2.b of the README file.
* It is now required that you set up a builder in Eclipse so that the Artisan Instrumentation aspectj file will be re-generated each time your app builds.

## Version 1.1.7

* (9/20/2013) Upgraded to Jackson 2.2.2 library.

## Version 2.0.3

* Adds the ArtisanTrackingManager to the public API so that you can track custom analytics events with Artisan.

## Version 2.0.10

* Adds BETA support for the Artisan Canvas.

## Version 2.0.11

* Add BETA support for IntelliJ IDEA Ultimate Edition installation so that developers can use either Eclipse or IntelliJ for Android development with Artisan.
* Significant performance improvements.

## Version 2.0.13

* Add BETA support for ANT installation.

## Version 2.0.14

* Add support for creating Optimize experiments on Power Hooks.

## Version 2.0.15

* Enable installation on apps that are using ProGuard. Documentation at http://docs.useartisan.com/dev/proguard-for-android

## Version 2.0.16

* Updated the format of analytics events for Context menu item clicks in the AspectJ version of Artisan

## Version 2.0.18

* MAJOR CHANGE: The default version of the Android SDK no longer depends on AspectJ. You can still enjoy all the features of Artisan including analytics, experiments and personalization, the Artisan Canvas and Power Hooks and all of the in-code API. You will have to update your Activities and add code to track some of the analytic events that used to  be captured automatically, like for button taps and menu selections. Basic analytics for users, sessions, devices and OS versions and screen views are still collected automatically. Details at http://docs.useartisan.com/dev/quickstart-for-android/
* Artisan auto-instrumentation with AspectJ is still available if you pass in the flag --aspectj when you run the installer. Details at http://docs.useartisan.com/dev/android-aspectj/
* We have also bundled and obfuscated our dependencies so that your APK's size will be leaner than ever before and you will no longer have to deal with version conflicts of your dependencies. If you are upgrading from an earlier version of Artisan you can remove any Artisan dependencies that your app doesn't otherwise depend upon. Details at http://docs.useartisan.com/dev/android-upgrade/



