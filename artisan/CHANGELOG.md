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

## Version 2.1.0

* MAJOR CHANGE: The default version of the Android SDK no longer depends on AspectJ. You can still enjoy all the features of Artisan including analytics, experiments and personalization, the Artisan Canvas and Power Hooks and all of the in-code API. You will have to update your Activities and add code to track some of the analytic events that used to  be captured automatically, like for button taps and menu selections. Basic analytics for users, sessions, devices and OS versions and screen views are still collected automatically. Details at http://docs.useartisan.com/dev/quickstart-for-android/
* Artisan auto-instrumentation with AspectJ is still available if you pass in the flag --aspectj when you run the installer. Details at http://docs.useartisan.com/dev/android-aspectj/
* We have also bundled and obfuscated our dependencies so that your APK's size will be leaner than ever before and you will no longer have to deal with version conflicts of your dependencies. If you are upgrading from an earlier version of Artisan you can remove any Artisan dependencies that your app doesn't otherwise depend upon. Details at http://docs.useartisan.com/dev/android-upgrade/

## Version 2.1.1

* We have moved the Artisan configuration from a subclass of the Artisan Service to your Application class. Configuring your Artisan ID, registering Power Hooks, In-code experiments and user profile variables will take place there instead of in your CustomArtisanService. Details at http://docs.useartisan.com/dev/quickstart-for-android/
* We have added support for advanced segmentation and personalization with the ArtisanProfileManager

## Version 2.1.2

* Minor release with bug fix for location-based segmentation and personalization.

## Version 2.1.3

* Added preliminary support for Artisan Push. Recommended to upgrade to 2.1.4 if you would like to take advantage of Campaigns and Artisan Push.

## Version 2.1.4

* We have added support for Artisan Push Messaging. Now you can send targeted push messages via Artisan to your users. Push messages will appear in the notification center and can trigger app opening and/or the executing of Artisan Power Hooks.

## Version 2.1.5

* Minor release. Sending device id when authenticating with Artisan Tools as a precursor to device whitelisting.

## Version 2.1.6

* Add ArtisanFragmentActivity as convenient base class for apps that use FragmentActivities.
* Minor fix for when the profile file is loaded at the first startup.

## Version 2.1.7

* Added ArtisanPurchaseWorkflowManager for tracking of commerce-specific analytics.
* Added ArtisanSocialSharingManager for tracking of social share events for analytics.
* Added playlist and power hook callbacks for Android! Now you can register for callbacks when the first playlist is downloaded, or when you power hook values change. You can even grab a reference to an individual power hook and register a call back for when that specific value changes.
* We now support the option to automatically clear a notification after it is opened. Look for the option when you are creating your Artisan Push Campaign.
* Fixed bug with Push Notification open actions so that the app is actually launched. This defect was introduced when we rolled out the auto-clear feature above. If you are using Artisan Push we recommend that you upgrade to Android 2.1.7 to get this fix.

## Version 2.1.8

* Fixed critical bug that was preventing the Artisan Playlist from downloading for some customers. This bug was introduced in 2.1.7, so you should upgrade if you were using 2.1.7.

## Version 2.1.9

* Added optional flags to the API so that you can disable the artisan gesture in production and prevent the Artisan Gesture overlay from being added to your Activities. For non-production builds there is also a new disableGestureAndLoginOnForeground method that is an alternative for connecting to Artisan without the gesture. 

## Version 2.2.0

* Added support for the Artisan Hybrid SDK -- now you can use Artisan's APIs in your webviews!

