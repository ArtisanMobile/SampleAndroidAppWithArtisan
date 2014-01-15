This is a sample app with the Artisan Android SDK pre-installed.

It is intended to be used as a sandbox to see the Artisan SDK in action.

Prerequisites
==============

1) Eclipse with Android SDK (make sure you have API level 17 installed)
2) Add the AspectJ eclipse plugin:
  * In Eclipse click Help -> Install New Software
  * Click "Add" on the right to add a new repository.
    * For the name, you can name this AspectJ.
    * For the location, paste the url http://download.eclipse.org/tools/ajdt/42/update
    * Click "OK"
  * Select "AspectJ Developer Tools (Required)" and click "Next" and then "Next" again
  * Accept the licenses and click "Finish"
  * You may be prompted to restart Eclipse after this installation is complete.


Usage
==============

We have already run the Artisan Installer for you, so you can just look at the code and run the app.

* If you look at SimpleUpActivity to see an example of using the API to check isCurrentVariantForExperiment
and using that information to change the background of the simple up activity from the default to red or green.
There you can also see that we are using setExperimentViewedForExperiment onResume, so that every time a user views this activity we will count a view.
* If you look at NotificationActivity you can see where we are using setTargetReachedForExperiment to log that the user has reached the goal for this experiment.
* You can run this app without connecting to ArtisanTools. If you'd like to see how isCurrentVariant works you can pretent to activiate different variations using startExperiment in CustomArtisanService


Connecting to Artisan Tools
============================

Replace the AppID in CustomArtisanService with your AppID.

You can find your AppID by looking at the URL when you click on your app from the landing page.
For example, https://artisantools.com/apps/518bedaaa5eead368700000a <-- that last part is your AppID

* An experiment will be counted as "viewed" when you go to the SimpleUpActivity.
* The goal of this experiment is to get people to post more direct notifications.
  So, to trigger a conversion for this experiment post a direct notification from the notifications activity.
