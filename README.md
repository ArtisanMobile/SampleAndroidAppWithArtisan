This is a sample app with the Artisan Android SDK pre-installed.

It is intended to be used as a sandbox to see the Artisan SDK in action.

Prerequisites
==============

* IDE set up to build and run Android Apps. This app builds against API 19 so make sure you have that installed.


Usage
==============

We have already run the Artisan Installer for you, so you can just look at the code and run the app.

Open this project in your favorite Android IDE.

In this app we have example usage of our in-code api and power hooks

IN-CODE EXPERIMENTS
* In code experiments are registered in the CustomArtisanService class
* If you look at StoreDetailActivity to see an example of using the API to check isCurrentVariantForExperiment in the onResume method to make the appropriate changes to the Buy Now button
and using that information to change the background of the simple up activity from the default to red or green.
There you can also see that we are using setExperimentViewedForExperiment onResume, so that every time a user views this activity we will count a view.
* If you look at StoreDetailActivity you can see where we are using addSelectedItemToCartAndCheckout to log that the user has reached the goal for this experiment.
* You can run this app without connecting to ArtisanTools. If you'd like to see how isCurrentVariant works you can pretend to activate different variations using startExperiment in CustomArtisanService
* You will have to connect your app to Artisan to officially start an experiment and gather statistics about which variation is the most successful.

POWER HOOKS
* Power Hooks are registered in the CustomArtisanService
* In the StoreDetailActivity addSelectedItemToCart method you can see where we are using a Power Hook block to display a toast message when an item is added to the cart. The parameters of that block are configurable in real time from Artisan Tools.
* There is also a simple example of a Power Hook Variable in use on the AboutActivity's onResume Method. This text is configurable from  Artisan Tools

ANALYTICS
* App analytics are automatically captured and sent to the Artisan service. That means every user, session, page view, navigation and click is automatically captured by Artisan.
* Look for usages of the ArtisanTrackingManager for tracking custom analytics events.

ARTISAN CANVAS EXPERIMENTS
* You will need to connect your device to try out the Artisan Canvas. This is where you can create AB tests and publish changes to your app without having to touch the code at all.


Connecting to Artisan Tools
============================

You'll need to connect to Artisan Tools to try out the Artisan Canvas experiments, make changes to power hook values, and view your application analytics.

IMPORTANT: First you'll need to replace the AppID in CustomArtisanService with your AppID. You can find your App Id in the getting started notification that you got when you first created your new app in Artisan Tools.
