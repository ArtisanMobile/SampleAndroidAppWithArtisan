This is a sample app with the Artisan Android SDK pre-installed.

It is intended to be used as a sandbox to see the Artisan SDK in action.

Prerequisites
==============

1. Eclipse with Android SDK (At a minimum ensure you have support for Android 2.3.3 and build against the latest Android SDK)
2. Add the AspectJ eclipse plugin:
  * In Eclipse click Help -> Install New Software
  * Click "Add" on the right to add a new repository.
    * For the name, you can name this AspectJ.
    * For the location, you need the correct AJDT Update Site URL for your version of Eclipse:
      * Eclipse 4.3 (Kepler) Update Site URL:  http://download.eclipse.org/tools/ajdt/43/update
      * Eclipse 3.8 and 4.2 (Juno) Update Site URL:  http://download.eclipse.org/tools/ajdt/42/update
      * Eclipse 3.7 (Indigo) Update Site URL:  http://download.eclipse.org/tools/ajdt/37/update
      * For other versions of Eclipse find the correct Update Site URL on this page http://eclipse.org/ajdt/downloads/
    * Click "OK"
  * Select "AspectJ Developer Tools (Required)" and click "Next" and then "Next" again
  * Accept the licenses and click "Finish"
  * You may be prompted to restart Eclipse after this installation is complete.


Usage
==============

We have already run the Artisan Installer for you, so you can just look at the code and run the app.

However to make sure that all of the project settings are updated for your local machine you will need to complete this step:
* In a terminal, go to the artisan directory inside your project's root directory and run:
    install.bat (on Windows)
    sh install.sh (on Mac/OSX or linux)

Then you should be ready to try out the Artisan Demo app! Don't forget to refresh when you come back to Eclipse.

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

IMPORTANT: First you'll need to replace the AppID in CustomArtisanService with your AppID. You can find your AppID by looking at the URL when you click on your app from the landing page.
For example, https://artisantools.com/apps/518bedaaa5eead368700000a <-- that last part is your AppID
