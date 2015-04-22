This is a sample app with the Artisan Android SDK pre-installed.

It is intended to be used as a sandbox to see the Artisan SDK in action.

Prerequisites
==============

* Android Studio installed and ready to run Android Apps. This app builds against API 22 so make sure you have that installed.

Usage
==============

We have already run the Artisan Installer for you, so you can just look at the code and run the app.

Open this project in Android Studio. All of the required dependencies are in the app/libs directory or handled  by gradle.

In this app we have example usage of our APIs for analytics, Power Hooks, and experiments.

ANALYTICS
* App analytics are automatically captured and sent to the Artisan service. That means every user, session, and page view is automatically captured by Artisan.
* Look for usages of the ArtisanTrackingManager for tracking custom analytics events for button click handlers. See the CheckoutActivity.onCheckout method for an example
* The ArtisanPurchaseWorkflowManager is designed to track purchase-related events. You can see a cart checkout tracked in CheckoutActivity.onCheckout and individual product events tracked on the ShoppingCart model addItem and removeItem methods.

POWER HOOKS
* Power Hooks are registered in the ArtisanDemoApplication
* In the StoreDetailActivity addSelectedItemToCart method you can see where we are using a Power Hook block to display a toast message when an item is added to the cart. The parameters of that block are configurable in real time from Artisan Tools.
* There is also a simple example of a Power Hook Variable in use on the AboutActivity's onResume Method. This text is configurable from  Artisan Tools

IN-CODE EXPERIMENTS
* In code experiments are registered in the ArtisanDemoApplication class
* If you look at StoreDetailActivity to see an example of using the API to check isCurrentVariantForExperiment in the onResume method to make the appropriate changes to the Buy Now button
and using that information to change the background of the simple up activity from the default to red or green.
There you can also see that we are using setExperimentViewedForExperiment onResume, so that every time a user views this activity we will count a view.
* If you look at StoreDetailActivity you can see where we are using addSelectedItemToCartAndCheckout to log that the user has reached the goal for this experiment.
* You can run this app without connecting to ArtisanTools. If you'd like to see how isCurrentVariant works you can pretend to activate different variations using startExperiment in ArtisanDemoApplication
* You will have to connect your app to Artisan to officially start an experiment and gather statistics about which variation is the most successful.

ARTISAN PUSH MESSAGE CAMPAIGNS
* This app is not configured by default to use Artisan Push. If you would like to try Artisan Push with this app you can re-run the installer and answer "y" when it asks about Artisan Push. You will need to add the Google Play Services Library as a dependency and add your GCM Sender ID to ArtisanDemoApplication.onCreate (see the instructions there). You will also need to set up a corresponding app in Artisan Tools and add your GCM Server Key there.

Connecting to Artisan Tools
============================

You'll need to connect to Artisan Tools to try out the Artisan Canvas experiments, make changes to power hook values, and view your application analytics.

IMPORTANT: Before you can connect to Artisan you'll need to replace the AppID in ArtisanDemoApplication.onCreate with your AppID. You can find your App Id in the getting started notification that you got when you first created your new app in Artisan Tools.
