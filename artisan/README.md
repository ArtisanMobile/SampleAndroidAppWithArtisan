# ARTISAN SDK INSTALLATION

----------------------------------------------------------------------------------------------------

Thank you for using Artisan!  This installer will automatically integrate the Artisan SDK into your application, enabling you to connect to Artisan and begin testing and optimizing your native Android app!

These instructions and more reference are available at http://docs.useartisan.com/dev/quickstart-for-android

## Table of Contents

--------------------------------------------------

1. Prerequisites
2. Artisan installation instructions
	a. Automatic
	b. Manual
3. Sample usage of the Artisan library


##[1] Prerequisites

--------------------------------------------------

1. Android App with a minimum SDK of no lower than Android 2.3.3 (API 10) and build against the latest Android SDK.
2. Download the installer from Artisan Tools. You should have a file like: YourProjectName-ArtisanInstaller.zip

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

This will update your manifest file, add the Artisan jar to your libs and create an Application class for you if one doesn't exist already.

If there are any settings in your application's manifest that are not compatible with Artisan you will be notified and the installer will not complete. For example, you must specify a minimum Android SDK of 2.3.3 or higher. Change the specified settings and run the Artisan installer again.

NOTE FOR ECLIPSE USERS: You'll need to refresh the project in Eclipse so that the newly added files are pulled into the project. Right click on your project and choose "Refresh".

3. If you are using ProGuard you will need to follow additional configuration steps to get Artisan working for your release builds. See http://docs.useartisan.com/dev/proguard-for-android for more details

4. Update all of your activities to extend the ArtisanActivity or implement our interface ArtisanBoundActivity

In order for your app to be properly instrumented all of your activities need to either extend ArtisanActivity or implement the ArtisanBoundActivity interface.

The simpler option is to extend ArtisanActivity. There's nothing else you need to do if you are extending from ArtisanActivity.

Sample Activity that extends ArtisanActivity:

	import com.artisan.activity.ArtisanActivity;

	public class BaseActivity extends ArtisanActivity {
  	...
	}

If you are already extending a third party Activity or don't wish to extend the ArtisanActivity you have the alternative of implementing our interface instead.

If you do, you must also make sure to add implementations for each of these methods and call the respective ArtisanActivity static method:
* implement **protected void onCreate(Bundle savedInstanceState)** and call **ArtisanActivity.artisanOnCreate(this)**
* implement **protected void onStart()** and call **ArtisanActivity.artisanOnStart(this)**
* implement **protected void onStop()** and call **ArtisanActivity.artisanOnStop(this)**
* implement **protected void onDestroy()** and call **ArtisanActivity.artisanOnDestroy()**
* implement **public void setContentView(int layoutResID)** if you are using this version of setContentView for this Activity or its subclasses and get the contentView from **ArtisanActivity.artisanGetContentView(layoutResID, this)** and pass that in to super.setContentView(View)
* implement **public void setContentView(View view)** if you are using this version of setContentView for this Activity or its subclasses and get the contentview from **ArtisanActivity.artisanGetContentView(view, this)** and pass that in to super.setContentView(View)
* implement **public void setContentView(View view, LayoutParams params)** if you are using this version of setContentView for this Activity or its subclasses and get the contentView from **ArtisanActivity.artisanGetContentView(view, params, this)** and pass that in to super.setContentView(View)
* implement **public ArtisanService getArtisanService()** and return **ArtisanActivity._getArtisanService()**

Sample Activity that implements ArtisanBoundActivity:

		import android.app.Activity;
		import android.os.Bundle;
		import android.view.View;

		import com.artisan.activity.ArtisanActivity;
		import com.artisan.services.ArtisanBoundActivity;
		import com.artisan.services.ArtisanService;

		public class SampleArtisanCustomerActivity extends Activity implements ArtisanBoundActivity {

			@Override
			protected void onCreate(Bundle savedInstanceState) {
				super.onCreate(savedInstanceState);
				ArtisanActivity.artisanOnCreate(this);
				setContentView(R.layout.activity_absolute_layout);
			}

			@Override
			protected void onStart() {
				super.onStart();
				ArtisanActivity.artisanOnStart(this);
			}

			@Override
			protected void onStop() {
				super.onStop();
				ArtisanActivity.artisanOnStop(this);
			}

			@Override
			protected void onDestroy() {
				// Call Artisan method BEFORE onDestroy
				ArtisanActivity.artisanOnDestroy();
				super.onDestroy();
			}

			@Override
			public void setContentView(int layoutResID) {
				View contentView = ArtisanActivity.artisanGetContentView(layoutResID, this);
				super.setContentView(contentView);
			}

			@Override
			public void setContentView(View view) {
				View contentView = ArtisanActivity.artisanGetContentView(view, this);
				super.setContentView(contentView);
			}

			@Override
			public void setContentView(View view, LayoutParams params) {
				View contentView = ArtisanActivity.artisanGetContentView(view, params, this);
				super.setContentView(contentView);
			}

			@Override
			public ArtisanService getArtisanService() {
				return ArtisanActivity._getArtisanService();
			}
		}

**Congratulations! You are now ready to start using Artisan!**

##[2.b] Artisan installation instructions (manual)

--------------------------------------------------

### Copy Artisan assets to app

1. Unzip the contents of `YourProjectName-ArtisanInstaller.zip` into a folder in the root of your project directory.
2. Copy the artisan_library.jar file from the 'artisan/artisan_library' folder into your project's libs directory. You might need to create this folder in the project's root directory if it does not already exist.
3. Copy all Android resources from `artisan/androidResources/res` to your project's `res` directory. Be sure to keep the subfolder structure intact.
4. Copy all Android resources from `artisan/androidResources/assets` to your project's `assets` directory.

### Starting the Artisan service

In order for Artisan to run within your app, the Artisan service has to be started. To do this, we will create a subclass of `ArtisanService` and add a declaration in the manifest which points to this service.

1. Create an Application class for your app.

2. You will need to call ArtisanManager.startArtisan in the onCreate of your Application class. Replace "Your Artisan app id here" with the appropriate string. You can find your app id in Artisan Tools on the screen after you first create your app or on the settings page for your app:

    import com.artisan.application.ArtisanApplication;

    public class MySampleApplication extends ArtisanApplication {

      @Override
      public void onCreate() {
        super.onCreate();

        ArtisanManager.startArtisan(this, "YOUR_ARTISAN_APPID_HERE";
      }
    }

    /**
     * Register your Artisan Power Hook variables and Power Hook blocks here
     *
     * For example:
     *
     * PowerHookManager.registerVariable("WelcomeText", "Welcome Text Sample PowerHook", "Welcome to Artisan!");
     *
     * <code>
     *  HashMap<String, String> defaultData = new HashMap<String, String>();
     *  defaultData.put("discountCode", "012345ABC");
     *  defaultData.put("discountAmount", "25%");
     *  defaultData.put("shouldDisplay", "true");
     *
     *  PowerHookManager.registerBlock("showAlert", "Show Alert Block", defaultData, new ArtisanBlock() {
     *    public void execute(Map<String, String> data, Map<String, Object> extraData) {
     *      if ("true".equalsIgnoreCase(data.get("shouldDisplay"))) {
     *        StringBuilder message = new StringBuilder();
     *        message.append("Buy another for a friend! Use discount code ");
     *        message.append(data.get("discountCode"));
     *        message.append(" to get ");
     *        message.append(data.get("discountAmount"));
     *        message.append(" off your purchase of 2 or more!");
     *        Toast.makeText((Context) extraData.get("context"), message, Toast.LENGTH_LONG).show();
     *      }
     *    }
     * });
     * </code>
     *
     * More examples at http://docs.useartisan.com/dev/quickstart-for-android/#power-hooks
     *
     */
    @Override
    public void registerPowerhooks() {

    }

    /**
     * Register your Artisan In-code Experiments here
     *
     * For example:
     *
     * ArtisanExperimentManager.registerExperiment("my first experiment");
     * ArtisanExperimentManager.addVariantForExperiment("blue variation", "my first experiment");
     * ArtisanExperimentManager.addVariantForExperiment("green variation", "my first experiment");
     *
     * More examples at http://docs.useartisan.com/dev/quickstart-for-android/#in-code
     */
    @Override
    public void registerInCodeExperiments() {

    }

    /**
     * Register your Artisan In-code Experiments here
     *
     * For example:
     *
     * ArtisanProfileManager.registerDateTime("lastSeenAt", new Date());
     * ArtisanProfileManager.registerLocation("lastKnownLocation");
     * ArtisanProfileManager.registerNumber("totalOrderCount", ArtisanDemoApplication.totalOrderCount);
     * ArtisanProfileManager.registerString("visitorType", "anonymous");
     * ArtisanProfileManager.setGender(Gender.Female);
     * ArtisanProfileManager.setUserAge(29);
     * ArtisanProfileManager.setSharedUserId("abcdef123456789");
     * ArtisanProfileManager.setUserAddress("234 Market Street, Philadelphia, PA 19106");
     *
     * More examples at http://docs.useartisan.com/dev/quickstart-for-android/#api
     */
    @Override
    public void registerUserProfileVariables() {

    }

Alternatively, if you can't extend ArtisanApplication you can implement the com.artisan.application.ArtisanRegisteredApplication interface.

3. The last step is updating your AndroidManifest.xml so that Android knows where to find the service and has the correct permissions. Add the following line inside the `<application>` element, using the path to the ArtisanService class.

		<service android:name="com.artisan.services.ArtisanService"/>

You will also need to add the following permissions to your AndroidManifest.xml if they aren't already set:

    <uses-permission android:name="android.permission.GET_TASKS"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>

You can put these before the <application> tag.

### Update all of your activities to extend the ArtisanActivity or implement our interface ArtisanBoundActivity

In order for your app to be properly instrumented all of your activities need to either extend ArtisanActivity or implement the ArtisanBoundActivity interface.

The simpler option is to extend ArtisanActivity. There's nothing else you need to do if you are extending from ArtisanActivity.

Sample Activity that extends ArtisanActivity:

	import com.artisan.activity.ArtisanActivity;

	public class BaseActivity extends ArtisanActivity {
		...
	}

If you are already extending a third party Activity or don't wish to extend the ArtisanActivity you have the alternative of implementing our interface instead.

If you do, you must also make sure to add implementations for each of these methods and call the respective ArtisanActivity static method:
* implement **protected void onCreate(Bundle savedInstanceState)** and call **ArtisanActivity.artisanOnCreate(this)**
* implement **protected void onStart()** and call **ArtisanActivity.artisanOnStart(this)**
* implement **protected void onStop()** and call **ArtisanActivity.artisanOnStop(this)**
* implement **protected void onDestroy()** and call **ArtisanActivity.artisanOnDestroy()**
* implement **public void setContentView(int layoutResID)** if you are using this version of setContentView for this Activity or its subclasses and get the contentView from **ArtisanActivity.artisanGetContentView(layoutResID, this)** and pass that in to super.setContentView(View)
* implement **public void setContentView(View view)** if you are using this version of setContentView for this Activity or its subclasses and get the contentview from **ArtisanActivity.artisanGetContentView(view, this)** and pass that in to super.setContentView(View)
* implement **public void setContentView(View view, LayoutParams params)** if you are using this version of setContentView for this Activity or its subclasses and get the contentView from **ArtisanActivity.artisanGetContentView(view, params, this)** and pass that in to super.setContentView(View)
* implement **public ArtisanService getArtisanService()** and return **ArtisanActivity._getArtisanService()**

Sample Activity that implements ArtisanBoundActivity:

		import android.app.Activity;
    import android.os.Bundle;
    import android.view.View;

    import com.artisan.activity.ArtisanActivity;
    import com.artisan.services.ArtisanBoundActivity;
    import com.artisan.services.ArtisanService;

    public class SampleArtisanCustomerActivity extends Activity implements ArtisanBoundActivity {

      @Override
      protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArtisanActivity.artisanOnCreate(this); // Call Artisan method AFTER super.onCreate
        setContentView(R.layout.activity_absolute_layout);
      }

      @Override
      protected void onStart() {
        super.onStart();
        ArtisanActivity.artisanOnStart(this); // Call Artisan method AFTER super.onStart
      }

      @Override
      protected void onStop() {
        super.onStop();
        ArtisanActivity.artisanOnStop(this); // Call Artisan method AFTER super.onStop
      }

      @Override
      protected void onDestroy() {
        ArtisanActivity.artisanOnDestroy(); // Call Artisan method BEFORE super.onDestroy
        super.onDestroy();
      }

      /**
       * This is the version of setContentView that most people use.
       * You only need to implement this if you are using this version of setContentView in this Activity or its subclasses.
       * If you are using it it looks something like this:
       * setContentView(R.layout.activity_main);
       */
      @Override
      public void setContentView(int layoutResID) {
        View contentView = ArtisanActivity.artisanGetContentView(layoutResID, this);
        super.setContentView(contentView);
      }

      // You only need to implement this if you are using this version of setContentView in this Activity or its subclasses.
      @Override
      public void setContentView(View view) {
        View contentView = ArtisanActivity.artisanGetContentView(view, this);
        super.setContentView(contentView);
      }

      // You only need to implement this if you are using this version of setContentView in this Activity or its subclasses.
      @Override
      public void setContentView(View view, LayoutParams params) {
        View contentView = ArtisanActivity.artisanGetContentView(view, params, this);
        super.setContentView(contentView);
      }

      /**
       * This method is required by the ArtisanBoundActivity interface.
       * You can copy this implementation as is to your Activities that extend our interface.
       */
      @Override
      public ArtisanService getArtisanService() {
        return ArtisanActivity._getArtisanService();
      }
    }

**Congratulations! You are now ready to start using Artisan!**

##[4] Sample usage of Artisan library to register experiments

--------------------------------------------------

If you are using the in-code API for an Artisan Optimize experiment, you must register your experiments in the Application class. The ArtisanRegisteredApplication provides a callback for you in the form of `registerInCodeExperiments`:

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

For Artisan API documentation, please see http://docs.useartisan.com/dev/quickstart-for-android/#api

##[5] Sample usage of Artisan Power Hooks

--------------------------------------------------

Power Hooks are registered in your Application class. The ArtisanRegisteredApplication interface provides a callback for you in the form of `registerPowerhooks`:

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
