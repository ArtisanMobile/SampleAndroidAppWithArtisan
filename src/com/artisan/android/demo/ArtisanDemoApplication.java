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
import android.content.Intent;
import android.content.res.AssetManager;
import android.util.Log;
import android.widget.Toast;

import com.artisan.android.demo.activity.CheckoutActivity;
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

		// Remove this to enable Artisan Push
		ArtisanManager.disableArtisanPush();
		// Uncomment this to enable Artisan Push
		// ArtisanManager.setPushSenderId("PUT_YOUR_GOOGLE_PROJECT_NUMBER_HERE"); // Your sender ID is your Google Project Number. See http://developer.android.com/google/gcm/gs.html
		// If you would like to use Artisan Push there are a few more permissions that need to be in your manifest and you need to add the google play support library to your app.
		// See http://docs.useartisan.com/dev/android/push-notifications for details.

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
		// ArtisanProfileManager.setGender(Gender.Female);
		// ArtisanProfileManager.setUserAge(29);
		ArtisanProfileManager.setSharedUserId("abcdef123456789");
		ArtisanProfileManager.setUserAddress("234 Market Street, Philadelphia, PA 19106");

		// You can also register and set separately using the set methods. These are just a few examples
		ArtisanProfileManager.setDateTimeValue("lastSeenAt", new Date());
		// ArtisanProfileManager.setLocationValue("lastKnownLocation", new ArtisanLocationValue(39.949920, -75.145102));
	}

	@Override
	public void registerPowerhooks() {
		PowerHookManager.registerVariable("welcome_text", "Welcome Text Sample PowerHook", "Welcome to Artisan!");
		PowerHookManager.registerVariable("purchase_thanks", "Thank you message after purchase", "Thank you for your purchase! Your order is on its way.");

		PowerHookManager.registerVariable("request_demo_heading", "Text at the top of the request demo screen", this.getString(R.string.request_demo_heading));

		PowerHookManager.registerVariable("store_detail_checkout", "Buy now button text", this.getString(R.string.store_detail_checkout));
		PowerHookManager.registerVariable("store_detail_add_to_cart", "Add to cart button text", this.getString(R.string.store_detail_add_to_cart));
		PowerHookManager.registerVariable("checkout_submit", "Checkout button text", this.getString(R.string.checkout_submit));
		PowerHookManager.registerVariable("cart_item_remove", "Remove cart item text", this.getString(R.string.cart_item_remove));
		PowerHookManager.registerVariable("cart_empty", "Cart empty text", this.getString(R.string.cart_empty));
		PowerHookManager.registerVariable("cart_total", "Cart total text", this.getString(R.string.cart_total));

		PowerHookManager.registerVariable("visit_website", "Visit website button text", this.getString(R.string.visit_website));
		PowerHookManager.registerVariable("website_url", "Website URL", this.getString(R.string.website_url));

		HashMap<String, String> defaultData = new HashMap<String, String>();
		defaultData.put("message", "Check out now and get ##discountAmount## off of your purchase. We've added discount code ##discountCode## to your cart.");
		defaultData.put("discountCode", "012345ABC");
		defaultData.put("discountAmount", "10%");
		defaultData.put("shouldDisplay", "false");
		PowerHookManager.registerBlock("showDiscountMessage", "Show discount message", defaultData, new ArtisanBlock() {
			public void execute(Map<String, String> data, Map<String, Object> extraData, Context context) {
				if ("true".equalsIgnoreCase(data.get("shouldDisplay"))) {
					String message = data.get("message");
					message = message.replace("##discountCode##", data.get("discountCode"));
					message = message.replace("##discountAmount##", data.get("discountAmount"));
					Toast.makeText(context, message, Toast.LENGTH_LONG).show();
				}
			}
		});

		/*
		 * Sample push payload for demo {"ANA":{"PH":"showCartWithMessage" : "PHP" : { "message" : "Check out now and get ##discountAmount## off your purchase. We've added discount code ##discountCode## to your cart.", "discountCode" : "0000123", "discountAmount" : "30%" }} }
		 */
		HashMap<String, String> defaultShowCartData = new HashMap<String, String>();
		defaultShowCartData.put("message", "Check out now and get ##discountAmount## off of your purchse. We've added discount code ##discountCode## to your cart.");
		defaultShowCartData.put("discountCode", "012345ABC");
		defaultShowCartData.put("discountAmount", "25%");
		PowerHookManager.registerBlock("showCartWithMessage", "Show cart and display message", defaultShowCartData, new ArtisanBlock() {
			public void execute(Map<String, String> data, Map<String, Object> extraData, Context context) {
				Intent startCartIntent = new Intent(context, CheckoutActivity.class);
				startCartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(startCartIntent);

				String message = data.get("message");
				message = message.replace("##discountCode##", data.get("discountCode"));
				message = message.replace("##discountAmount##", data.get("discountAmount"));
				Toast.makeText(context, message, Toast.LENGTH_LONG).show();

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