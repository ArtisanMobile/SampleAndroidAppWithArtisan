# ARTISAN SDK INSTALLATION

----------------------------------------------------------------------------------------------------

Thank you for using Artisan!  This installer will automatically integrate the Artisan SDK into your application, enabling you to connect to Artisan and begin testing and optimizing your native Android app!

## Table of Contents

--------------------------------------------------

1. Prerequisites
2. Artisan installation instructions
	a. Automatic
	b. Manual
3. Sample usage of the Artisan library


##[1] Prerequisites

--------------------------------------------------

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
3. Download the installer from Artisan Tools. You should have a file like: YourProjectName-ArtisanInstaller.zip

Please note: the Artisan Android SDK is not compatible with ProGuard. Unexpected behavior may occur if you install Artisan into an app built with ProGuard.

Also note, the Artisan SDK uses the following jars, which will be copied in to your project:
  * android-support-v4.jar
  * httpclientandroidlib-1.1.2.jar
  * jackson-annotations-2.2.3.jar
  * jackson-core-2.2.3.jar
  * jackson-core-asl-1.9.7.jar
  * jackson-databind-2.2.3.jar
  * jackson-mapper-asl-1.9.7.jar
  * otto-1.3.4.jar
  * volley.jar (built from source)
If you are using library projects that have the same dependencies you will need to manually ensure that the dependencies do not conflict. We can only guarantee that Artisan works with these versions.

The following permissions are required for the Artisan Android SDK to function properly:
  * GET_TASKS - We use this permission to detect when the app is going into the background. This allows us to download and apply view changes in a timely and seamless fashion.
  * WRITE_EXTERNAL_STORAGE - This permission is required to save the changeset data
  * INTERNET - Artisan uses the internet to download change data for modifications and A/B testing
  * ACCESS_NETWORK_STATE - This permission allows us to be smart about when we connect to the internet to download changes

##[2.a] Artisan installation instructions (automatic)

--------------------------------------------------

Artisan comes bundled with an installer that will configure your IDE and add the necessary files to your source root. It will also edit your AndroidManifest.xml to point to the CustomArtisanService that is needed to instrument your Artisan application. For most cases, we recommend using the installer, but if you prefer to configure the project yourself, please skip forward to the next section.

1. Extract the contents of YourProjectName-ArtisanInstaller.zip (the directory that contains this readme) into your project's root directory.
2. In a terminal, go to the artisan directory inside your project's root directory and run:

    install.bat (on Windows)
    sh install.sh (on Mac/OSX or linux)

This will update your manifest file, add the required libraries and create the CustomArtisanService class, if one doesn't exist already. It will also configure an Eclipse 'Builder' that will regenerate necessary AspectJ declarations for your Activities after every build. See "Configuring the Artisan build script" in section [2.b] for more information.

Note: This process will automatically update the visibility of the onStart, onStop and onDestroy methods of your activities so that they can be instrumented by Artisan.

If there are any settings in your application's manifest that are not compatible with Artisan you will be notified and the installer will not complete. For example, you must specify a minimum Android SDK of 2.3.3 or higher. Change the specified settings and run the Artisan installer again.

3. Installation is now complete. You'll need to refresh the project in Eclipse so that the newly added files are pulled into the project. Right click on your project and choose "Refresh".

**Congratulations! You are now ready to start using Artisan!**

##[2.b] Artisan installation instructions (manual)

--------------------------------------------------

### Eclipse Project configuration

1. Unzip the contents of `YourProjectName-ArtisanInstaller.zip` into a folder in the root of your project directory.
2. Copy the files from the 'artisan/Support' folder into your project's libs directory. You might need to create this folder in the project's root directory if it does not already exist. If you are using library projects that have the same dependencies as Artisan you will need to manually ensure that the dependencies do not conflict. We can only guarantee that Artisan works with these versions.
3. Copy all Android resources from `artisan/androidResources/res` to your project's `res` directory. Be sure to keep the subfolder structure intact.
4. Copy all Android resources from `artisan/androidResources/assets` to your project's `assets` directory.
5. Add the AspectJ nature to the Eclipse project:
	* Open Eclipse. Right-click on your project in the Project Explorer and choose "Configure -> Convert to AspectJ Project".
	* If Eclipse prompts you whether to include AspectJ files on the build path, select "yes".
6. Add the artisan library to the build path:
	* Right click on your project in the Project Explorer and choose "Properties".
	* In the list on the left, click on "AspectJ build".
	* Make sure that the "Inpath" tab is selected.
	* If the artisan library jar is not on the inpath, click "Add jars" and add artisan/artisan_library/artisan_library_4.jar.
7. Export the AspectJ Runtime Library
  * Right click on your project in the Project Explorer and choose "Properties".
  * In the list on the left, click on "Java Build Path".
  * Make sure that the "Order and Export" tab is selected.
  * If the "AspectJ Runtime Library" is not already selected, check it and press OK.
8. Make your Activity lifecycle methods public. In order to instrument your application Artisan needs any onStart, onStop and onDestroy methods that you have implemented in your app to be public. The default for the lifecycle methods is protected, which we are not able to instrument. If you skip this step Artisan will not work properly and you may experience errors and you may have unexpected behavior.

### Starting the Artisan service

In order for Artisan to run within your app, the Artisan service has to be started. To do this, we will create a subclass of `ArtisanService` and add a declaration in the manifest which points to this service.

1. Create a class called 'CustomArtisanService' inside one of your source packages. This class needs to extend com.artisan.services.ArtisanService. If you would like to call it something else, you must edit the value of the 'artisan_service_name' string resource in 'res/values/artisan_ids.xml' to match the your custom class name.

2. You will need to implement one abstract method to start the Artisan service. Add the following method to your class, replacing \<Your artisan app id here\> with the appropriate string. You can find your app id in Artisan Tools on the screen after you first create your app or on the settings page for your app:

    import com.artisan.manager.ArtisanManager;
    import com.artisan.services.ArtisanService;

    public class CustomArtisanService extends ArtisanService {
      @Override
      public void startArtisanManager(ArtisanManager manager) { do
        artisanManager.start("<Your artisan app id here> ");
      }
    }

3. The last step is updating your AndroidManifest.xml so that Android knows where to find the service and has the correct permissions. Add the following line inside the `<application>` element, using the relative path to your concrete ArtisanService class.

		<service android:name=".path.to.my.CustomArtisanService"/>

You will also need to add the following permissions to your AndroidManifest.xml if they aren't already set:

    <uses-permission android:name="android.permission.GET_TASKS"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>

You can put these before the <application> tag.

### Configuring the Artisan build script

Artisan generates AspectJ files alongside your code. These files live inside the 'gen/' folder, next to your other generated code, such as R.java. It is very important that these files get generated or Artisan will not run in your application. In addition, this script will update the visibility of any new onStart, onStop and onDestroy methods on your activities so that we can instrument them automatically.

You can regenerate these files at any time by running the installer script with the `--aspectonly` flag.

In order to regenerate these files *automatically* after every build, Eclipse needs to be configured with a 'Builder' that will run the Artisan build script. Follow the instructions below to set up the Eclipse builder:

1. Create a new Eclipse builder
	* Right click on your project in the Project Explorer and choose "Properties".
	* Click on "Builders" and select "New…".
	* Eclipse will ask you to "Choose a type of external tool to create".
	* Choose "program" and click OK. The 'Edit Launch Configurations' dialog will appear.
	* Give your builder a name, such as "Artisan Build Script".
2. For the "location" field, click "Browse Workspace" and browse to your `artisan` folder.
	* On Mac/Linux, select `install.sh` and click OK.
	* On Windows, select `install.bat` and click OK.
3. For the "working directory" field, click on "browse workspace" and select your `artisan` folder. Click OK.
4. In the "arguments" text box, type `--aspectonly`. Click "OK" to save the builder configuration.
5. Move the builder to the top of the list, above "Android Resource Manager".

**Congratulations! You are now ready to start using Artisan!**

##[4] Sample usage of Artisan library to register experiments

--------------------------------------------------

If you are using the in-code API for an Artisan Optimize experiment, you must register your experiments in the CustomArtisanService class. The ArtisanService provides a callback for you in the form of `registerInCodeExperiments`:

	@Override
	public void registerInCodeExperiments() {
		ArtisanExperimentManager.registerExperiment("Purchase Button Experiment");
		ArtisanExperimentManager.addVariantForExperiment("Original Text and Size", "Purchase Button Experiment");
		ArtisanExperimentManager.addVariantForExperiment("New Text", "Purchase Button Experiment");
		ArtisanExperimentManager.addVariantForExperiment("Bigger Button", "Purchase Button Experiment");
	}

In your activities, you can code the variations like this:

  Button buyButton = (Button) findViewById(R.id.buyButton);
  if(ArtisanExperimentManager.isCurrentVariantForExperiment("Original Text and Size", "Purchase Button Experiment")) {
    buyButton.setText( R.string.buyButtonText );
	} else if(ArtisanExperimentManager.isCurrentVariantForExperiment("New Text", "Purchase Button Experiment")) {
		buyButton.setText("Buy It Now!");
	} else if (ArtisanExperimentManager.isCurrentVariantForExperiment("Bigger Text", "Purchase Button Experiment")) {
    buyButton.setScaleX(1.5f);
    buyButton.setScaleY(1.5f);
  }

To record the fact that an experiment has been viewed, call "setExperimentViewedForExperiment". The Activity.onStart method is a good place for this.

    @Override
    protected void onStart() {
        super.onStart();
        ArtisanExperimentManager.setExperimentViewedForExperiment("Purchase Button Experiment");
    }

To indicate that a conversion has occurred:

    ArtisanExperimentManager.setTargetReachedForExperiment("Purchase Button Experiment");

For example, in your onCreate method, you might wish to record a button click as a conversion:

    buyButton.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            ArtisanExperimentManager.setTargetReachedForExperiment("Purchase Button Experiment");
            //perform "buy" business logic
        }
    });

You should now be able to run the app and connect to Artisan Tools.

For Artisan API documentation, please see https://getsatisfaction.com/artisan/topics/artisan_api_documentation.

##[5] Sample usage of Artisan Power Hooks

--------------------------------------------------

Power Hooks are registered in your CustomArtisanService class. The ArtisanService provides a callback for you in the form of `registerPowerhooks`:

To register a Power Hook Variable:
  PowerHookManager.registerVariable("addToCartButton", "Add To Cart Button Text", "Buy Now");

To register a Power Hook Block:
    PowerHookManager.registerBlock("showAlert", "Show Alert Block", defaultData, new ArtisanBlock() {
                @Override
                public void execute(Map<String, String> data, Map<String, Object> extraData) {
                        if ("true".equalsIgnoreCase(data.get("shouldDisplay"))) {
                                StringBuilder message = new StringBuilder();
                                if (extraData.get("productName") != null) {
                                        message.append("Buy another ");
                                        message.append(data.get("productName"));
                                        message.append(" for a friend! ");
                                }
                                message.append("Use discount code ");
                                message.append(data.get("discountCode"));
                                message.append(" to get ");
                                message.append(data.get("discountAmount"));
                                message.append(" off your next purchase!");
                                Toast.makeText((Context) extraData.get("context"), message, Toast.LENGTH_LONG).show();
                        }
                }
    });


Sample usage of a Power Hook Variable (in the onResume of my product detail page):

      Button cart_button = (Button) findViewById(R.id.AddToCartButton);
      cart_button.setText(PowerHookManager.getVariableValue("addToCartButton"));

NOTE: If you request the value for this Power Hook Variable in your first Activity's onCreate method, before the Artisan Service has a chance to start up, you may get a null value in return. The Artisan Service is started up at the time your first activity is created, and should be ready by the time your first activity gets to onResume. For the rest of your application's lifecycle you can assume that Power Hooks will work as expected.


Sample usage of a Power Hook Block (from the onClick handler for an add to cart button):

  Map<String, Object> extraData = new HashMap<String, Object>();
  extraData.put("productName", "Artisan Andy Plush Toy");
  extraData.put("context", this);
  PowerHookManager.executeBlock("showAlert", extraData);

NOTE: If you execute this Power Hook Block in your first Activity's onCreate method, before the Artisan Service has a chance to start up, the call may be ignored. The Artisan Service is started up at the time your first activity is created, and should be ready by the time your first activity gets to onResume. For the rest of your application's lifecycle you can assume that Power Hooks will work as expected.
