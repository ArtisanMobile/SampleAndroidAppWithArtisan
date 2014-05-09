package com.artisan.android.demo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.Toast;

import com.artisan.application.ArtisanApplication;
import com.artisan.incodeapi.ArtisanExperimentManager;
import com.artisan.incodeapi.ArtisanLocationValue;
import com.artisan.incodeapi.ArtisanProfileManager;
import com.artisan.incodeapi.ArtisanProfileManager.Gender;
import com.artisan.manager.ArtisanManager;
import com.artisan.powerhooks.ArtisanBlock;
import com.artisan.powerhooks.PowerHookManager;

public class ArtisanDemoApplication extends ArtisanApplication {

	private static final String TAG = ArtisanDemoApplication.class.getSimpleName();

	public static final String CONTROL_VARIANT = "Control";
	public static final String BUY_NOW_EXPERIMENT = "Buy Now Button";
	public static final String GREEN_VARIANT = "Green Button";
	public static final String ORANGE_VARIANT = "Orange Button";
	public static final String SKIP_DETAIL_EXPERIMENT = "Skip Detail";
	public static final String SKIP_DETAIL_VARIANT = "Skip Detail Variant";

	public static int totalOrderCount = 0;

	@Override
	public void onCreate() {
		super.onCreate();

		// Replace this with your AppID from artisan tools if you would like to try connecting and creating an in-code experiment
		// You can find your AppID by looking at the URL when you click on your app from the landing page.
		// https://artisantools.com/apps/52a5d8482b222086ae00001f <-- that last part is your AppID
		ArtisanManager.startArtisan(this, "52a5d8482b222086ae00001f");

		// pre-seeding the newsfeed from JSON data
		copyAssetToInternalStorage("news_feed.json");
	}

	private void copyAssetToInternalStorage(String filename) {
		AssetManager assetManager = getAssets();
		try {
			File outFile = new File(this.getFilesDir(), filename);
			if (!outFile.exists()) {
				Log.d(TAG, String.format("Copying %s from assets to internal storage.", filename));
				InputStream in = assetManager.open(filename);
				OutputStream out = new FileOutputStream(outFile);
				copyFile(in, out);
				in.close();
				out.flush();
				out.close();
			} else {
				Log.d(TAG, String.format("File %s already exists in internal storage, did not copy.", filename));
			}
		} catch(IOException e) {
			Log.e(TAG, "Failed to copy asset file: " + filename, e);
		}
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while((read = in.read(buffer)) != -1){
			out.write(buffer, 0, read);
		}
	}

	@Override
	public void registerUserProfileVariables() {
		ArtisanProfileManager.registerDateTime("lastSeenAt");
		ArtisanProfileManager.registerLocation("lastKnownLocation");
		ArtisanProfileManager.registerNumber("totalOrderCount", ArtisanDemoApplication.totalOrderCount);
		ArtisanProfileManager.registerString("visitorType", "anonymous");

		// These are just examples of using the . We don't have real profile information for this app
		// but you might for yours if you have authenticated users
		ArtisanProfileManager.setGender(Gender.Female);
		ArtisanProfileManager.setUserAge(29);
		ArtisanProfileManager.setSharedUserId("abcdef123456789");
		ArtisanProfileManager.setUserAddress("234 Market Street, Philadelphia, PA 19106");

		// You can also register and set separately using the set methods. These are just a few examples
		ArtisanProfileManager.setDateTimeValue("lastSeenAt", new Date());
		ArtisanProfileManager.setLocationValue("lastKnownLocation", new ArtisanLocationValue(39.949920, -75.145102));
	}

	@Override
	public void registerPowerhooks() {
		PowerHookManager.registerVariable("WelcomeText", "Welcome Text", "Welcome to Artisan!");
		PowerHookManager.registerVariable("PurchaseThanks", "Thank you message after purchase", "Thank you for your purchase! Your order is on its way.");
		PowerHookManager.registerVariable("requestDemoHeading", "Text at the top of the request demo screen", this.getString(R.string.request_demo_heading));

		HashMap<String, String> defaultData = new HashMap<String, String>();
		defaultData.put("discountCode", "012345ABC");
		defaultData.put("discountAmount", "25%");
		defaultData.put("shouldDisplay", "true");

		PowerHookManager.registerBlock("showAlert", "Show Alert Block", defaultData, new ArtisanBlock() {
			public void execute(Map<String, String> data, Map<String, Object> extraData) {
				if ("true".equalsIgnoreCase(data.get("shouldDisplay"))) {
					StringBuilder message = new StringBuilder();
					message.append("Buy another for a friend! Use discount code ");
					message.append(data.get("discountCode"));
					message.append(" to get ");
					message.append(data.get("discountAmount"));
					message.append(" off your purchase of 2 or more!");
					Toast.makeText((Context) extraData.get("context"), message, Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public void registerInCodeExperiments() {
		// Experiment on Store Detail screen for testing button changes.
		ArtisanExperimentManager.registerExperimentWithDescription(BUY_NOW_EXPERIMENT, "Experiment to test 'Buy Now' conversions on the Store Detail screen. \n" + "Removes the 'Add to Cart' button. \n" + "Changes the 'Buy Now' button to use different colors and take up the width of the screen");
		ArtisanExperimentManager.addVariantForExperiment(CONTROL_VARIANT, BUY_NOW_EXPERIMENT);
		ArtisanExperimentManager.addVariantForExperiment(GREEN_VARIANT, BUY_NOW_EXPERIMENT);
		ArtisanExperimentManager.addVariantForExperiment(ORANGE_VARIANT, BUY_NOW_EXPERIMENT);

		// You can use this code to try out different variations without connecting to Artisan Tools
		// ArtisanExperimentManager.startExperiment(BUY_NOW_EXPERIMENT, GREEN_VARIANT);

		// Experiment on Store screen to test UX flow.
		ArtisanExperimentManager.registerExperiment(SKIP_DETAIL_EXPERIMENT);
		ArtisanExperimentManager.addVariantForExperiment(CONTROL_VARIANT, SKIP_DETAIL_EXPERIMENT);
		ArtisanExperimentManager.addVariantForExperiment(SKIP_DETAIL_VARIANT, SKIP_DETAIL_EXPERIMENT);
	}

}